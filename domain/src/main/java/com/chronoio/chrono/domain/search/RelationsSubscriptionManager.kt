package com.chronoio.chrono.domain.search

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Marketplace
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.subscriptions.GlobalSubscription
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.domain.workspace.spaceIdOrNull
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class RelationsSubscriptionManager @Inject constructor(
    private val scope: CoroutineScope = GlobalScope,
    private val container: RelationsSubscriptionContainer,
    private val spaceManager: SpaceManager
): GlobalSubscription {
    val pipeline get() = spaceManager.state()
        .distinctUntilChangedBy { state -> state.spaceIdOrNull() }
        .flatMapLatest { state ->
        when(state) {
            is SpaceManager.State.Space.Active -> {
                // DROID-2916 TODO provide other spaces or add new subscription
                val params = buildParams(
                    space = SpaceId(state.config.space)
                )
                container.observe(params)
            }
            is SpaceManager.State.Space.Idle -> {
                // A space activated without Workspace.Open (quick capture). The params below
                // are built from the space id alone, so there is nothing a config would add.
                container.observe(buildParams(space = state.space))
            }
            is SpaceManager.State.NoSpace -> {
                flow {
                    container.unsubscribe()
                    emit(RelationsSubscriptionContainer.Index.empty())
                }
            }
            is SpaceManager.State.Init -> {
                emptyFlow()
            }
        }
    }

    private var job: Job? = null

    fun onStart() {
        job?.cancel()
        job = scope.launch { pipeline.collect() }
    }

    fun onStop() {
        scope.launch {
            container.unsubscribe()
            job?.cancel()
            job = null
        }
    }

    companion object {
        fun buildParams(space: SpaceId) = RelationsSubscriptionContainer.Params(
            space = space,
            subscription = RelationsSubscriptionContainer.SUBSCRIPTION_ID,
            filters = listOf(
                DVFilter(
                    relation = Relations.LAYOUT,
                    condition = DVFilterCondition.EQUAL,
                    value = ObjectType.Layout.RELATION.code.toDouble()
                ),
                DVFilter(
                    relation = Relations.IS_DELETED,
                    condition = DVFilterCondition.NOT_EQUAL,
                    value = true
                ),
                DVFilter(
                    relation = Relations.IS_ARCHIVED,
                    condition = DVFilterCondition.NOT_EQUAL,
                    value = true
                )
            ),
            limit = 0,
            offset = 0L,
            sorts = emptyList(),
            sources = emptyList(),
            keys = listOf(
                Relations.ID,
                Relations.SPACE_ID,
                Relations.TYPE,
                Relations.LAYOUT,
                Relations.NAME,
                Relations.RELATION_FORMAT,
                Relations.RELATION_KEY,
                Relations.SCOPE,
                Relations.IS_READ_ONLY,
                Relations.IS_HIDDEN,
                Relations.IS_DELETED,
                Relations.IS_ARCHIVED,
                Relations.IS_FAVORITE,
                Relations.RESTRICTIONS,
                Relations.MAX_COUNT,
                Relations.RELATION_READ_ONLY_VALUE,
                Relations.RELATION_DEFAULT_VALUE,
                Relations.RELATION_FORMAT_OBJECT_TYPES,
                Relations.SOURCE_OBJECT
            ),
            ignoreWorkspace = true
        )
    }
}