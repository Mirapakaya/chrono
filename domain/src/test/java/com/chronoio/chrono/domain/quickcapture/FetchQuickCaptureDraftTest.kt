package com.chronoio.chrono.domain.quickcapture

import com.chronoio.chrono.core_models.CoroutineTestRule
import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.multiplayer.ParticipantSubscriptionContainer
import com.chronoio.chrono.domain.util.dispatchers
import com.chronoio.chrono.test_utils.MockDataFactory
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

/**
 * The lookup is by id, and the creator check is applied to the result rather than pushed
 * into the query. These tests exist mostly to pin the FAIL-OPEN cases: a creator filter in
 * the query hid real drafts whenever `creator` was unset or our participant could not be
 * resolved, and an unfindable draft is indistinguishable from a space with no draft — which
 * is how one got orphaned by a second being created beside it.
 *
 * Stubs match the exact expected query: SpaceId is a value class, and a Mockito matcher for
 * one returns null, which NPEs when the call unboxes it.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FetchQuickCaptureDraftTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var rule = CoroutineTestRule()

    @Mock
    lateinit var repo: BlockRepository

    @Mock
    lateinit var participants: ParticipantSubscriptionContainer

    @Mock
    lateinit var auth: AuthRepository

    private lateinit var usecase: FetchQuickCaptureDraft

    private val space = SpaceId(MockDataFactory.randomUuid())
    private val draft = MockDataFactory.randomUuid()
    private val identity = MockDataFactory.randomUuid()
    private val participantId = MockDataFactory.randomUuid()
    private val keys = listOf(Relations.ID, Relations.CREATOR)

    @Before
    fun setup() {
        usecase = FetchQuickCaptureDraft(
            repo = repo,
            participants = participants,
            auth = auth,
            dispatchers = dispatchers
        )
    }

    private fun member(spaceId: String, id: String = participantId) = ObjectWrapper.SpaceMember(
        mapOf(
            Relations.ID to id,
            Relations.IDENTITY to identity,
            Relations.SPACE_ID to spaceId
        )
    )

    /** The query is by id alone — no creator filter, so nothing of ours can be hidden. */
    private fun stubSearch(result: List<Map<String, Any?>>) {
        repo.stub {
            onBlocking {
                searchObjects(
                    space = space,
                    filters = listOf(
                        DVFilter(
                            relation = Relations.ID,
                            value = draft,
                            condition = DVFilterCondition.EQUAL
                        )
                    ),
                    limit = 1,
                    keys = keys
                )
            } doReturn result
        }
    }

    private suspend fun run() = usecase.run(
        FetchQuickCaptureDraft.Params(space = space, draft = draft, keys = keys)
    )

    @Test
    fun `returns our own draft`() = runBlocking {
        auth.stub { onBlocking { getCurrentAccountId() } doReturn identity }
        participants.stub { on { get() } doReturn listOf(member(space.id)) }
        stubSearch(listOf(mapOf(Relations.ID to draft, Relations.CREATOR to participantId)))

        assertEquals(draft, run()?.id)
    }

    @Test
    fun `refuses a draft another participant created`() = runBlocking {
        auth.stub { onBlocking { getCurrentAccountId() } doReturn identity }
        participants.stub { on { get() } doReturn listOf(member(space.id)) }
        stubSearch(
            listOf(mapOf(Relations.ID to draft, Relations.CREATOR to MockDataFactory.randomUuid()))
        )

        assertNull(run(), "another member's draft must never be surfaced")
    }

    @Test
    fun `returns a draft whose creator is not set`() = runBlocking {
        auth.stub { onBlocking { getCurrentAccountId() } doReturn identity }
        participants.stub { on { get() } doReturn listOf(member(space.id)) }
        // No creator on the object: the veto cannot be certain, so it must not fire.
        stubSearch(listOf(mapOf(Relations.ID to draft)))

        assertEquals(draft, run()?.id, "an unset creator must not hide our own draft")
    }

    @Test
    fun `returns the draft when our participant cannot be resolved`() = runBlocking {
        // Identity unavailable and no participants loaded — the previous query-level filter
        // silently returned nothing here, which read as "this space has no draft".
        auth.stub { onBlocking { getCurrentAccountId() } doReturn identity }
        participants.stub { on { get() } doReturn emptyList() }
        participants.stub { on { observe() } doReturn kotlinx.coroutines.flow.emptyFlow() }
        stubSearch(listOf(mapOf(Relations.ID to draft, Relations.CREATOR to participantId)))

        assertEquals(draft, run()?.id, "an unresolvable participant must not hide the draft")
    }

    @Test
    fun `returns null when the store has no match`() = runBlocking {
        auth.stub { onBlocking { getCurrentAccountId() } doReturn identity }
        participants.stub { on { get() } doReturn listOf(member(space.id)) }
        stubSearch(emptyList())

        assertNull(run())
    }
}
