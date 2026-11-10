package com.chronoio.chrono.domain.quickcapture

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.DVSort
import com.chronoio.chrono.core_models.DVSortType
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.RelationFormat
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.multiplayer.ParticipantSubscriptionContainer
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Finds this account's unsent quick-capture drafts across every space, newest first.
 *
 * Drafts have always synced between devices; what they lacked was a property to ask for
 * them by, so each device could only reopen the one its own local pointer happened to name.
 * [Relations.IS_DRAFT] is that property, and this query is what makes a draft started on one
 * device reachable from another.
 *
 * Scoping is by creator, and the creator id is **per space** — the same account has a
 * different participant object in every space. A single id would therefore match drafts in
 * exactly one space and silently report none anywhere else, so the filter is an `IN` over
 * every participant this account owns. That list is already held account-wide by
 * [ParticipantSubscriptionContainer]; this stays one query, never a per-space fan-out.
 *
 * [Result.isComplete] carries `allStoresLoaded`. False means some per-space stores were still
 * warming: the answer is *unknown*, not *empty*, and callers must not treat it as "no drafts
 * exist" — creating a draft on a partial result is how a space ends up with two.
 */
class SearchQuickCaptureDrafts @Inject constructor(
    private val repo: BlockRepository,
    private val participants: ParticipantSubscriptionContainer,
    private val auth: AuthRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, SearchQuickCaptureDrafts.Result>(dispatchers.io) {

    data class Result(
        /** Newest first by creation date. */
        val drafts: List<ObjectWrapper.Basic>,
        /** False when per-space stores were still loading — treat as unknown, not empty. */
        val isComplete: Boolean
    ) {
        fun newestIn(space: Id): ObjectWrapper.Basic? =
            drafts.firstOrNull { it.spaceId == space }

        val spacesWithDrafts: Set<Id> get() = drafts.mapNotNull { it.spaceId }.toSet()
    }

    override suspend fun doWork(params: Unit): Result {
        val mine = ownParticipantIds()
        if (mine.isEmpty()) {
            // Cannot scope the query to ourselves, and an unscoped one would surface other
            // members' drafts in shared spaces. Report "unknown" rather than "none".
            return Result(drafts = emptyList(), isComplete = false)
        }
        val result = repo.crossSpaceSearch(
            Command.CrossSpaceSearch(
                filters = listOf(
                    DVFilter(
                        relation = Relations.IS_DRAFT,
                        value = true,
                        condition = DVFilterCondition.EQUAL
                    ),
                    DVFilter(
                        relation = Relations.CREATOR,
                        value = mine.toList(),
                        condition = DVFilterCondition.IN
                    )
                ),
                sorts = listOf(
                    DVSort(
                        relationKey = Relations.CREATED_DATE,
                        type = DVSortType.DESC,
                        includeTime = true,
                        relationFormat = RelationFormat.DATE
                    )
                ),
                limit = LIMIT,
                keys = KEYS
            )
        )
        return Result(drafts = result.records, isComplete = result.allStoresLoaded)
    }

    /** Every participant object belonging to this account, one per space it is a member of. */
    private suspend fun ownParticipantIds(): Set<Id> {
        val identity = runCatching { auth.getCurrentAccountId() }.getOrNull() ?: return emptySet()
        fun mine(members: List<ObjectWrapper.SpaceMember>): Set<Id> = members
            .filter { it.identity == identity }
            .map { it.id }
            .toSet()
        mine(participants.get()).takeIf { it.isNotEmpty() }?.let { return it }
        // Subscription not populated yet — wait briefly rather than answer wrongly.
        // firstOrNull, not first: `first` throws when the flow completes without a match.
        return withTimeoutOrNull(PARTICIPANT_AWAIT_TIMEOUT_MS) {
            participants.observe().firstOrNull { mine(it).isNotEmpty() }?.let(::mine)
        }.orEmpty()
    }

    companion object {
        const val PARTICIPANT_AWAIT_TIMEOUT_MS = 1_000L

        /**
         * Generous but bounded: an unlimited cross-space request materializes every space in
         * full. One draft per space per participant means this only binds for an account with
         * an implausible number of spaces.
         */
        const val LIMIT = 100

        val KEYS = listOf(
            Relations.ID,
            Relations.SPACE_ID,
            Relations.NAME,
            Relations.SNIPPET,
            Relations.CREATED_DATE,
            Relations.IS_DRAFT,
            Relations.IS_HIDDEN,
            Relations.IS_DELETED,
            Relations.IS_ARCHIVED,
            Relations.TYPE,
            Relations.LAYOUT
        )
    }
}
