package com.chronoio.chrono.presentation.home

import app.cash.turbine.test
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.StubConfig
import com.chronoio.chrono.core_models.StubObject
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_models.ui.ObjectIcon
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.library.StoreSearchParams
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.misc.UrlBuilderImpl
import com.chronoio.chrono.domain.objects.GetDateObjectByTimestamp
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.primitives.FieldParserImpl
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.spaces.GetSpaceView
import com.chronoio.chrono.domain.config.Gateway
import com.chronoio.chrono.domain.base.Resultat
import com.chronoio.chrono.presentation.search.ObjectSearchConstants
import com.chronoio.chrono.presentation.util.DefaultCoroutineTestRule
import com.chronoio.chrono.presentation.widgets.RecentlyEditedWidgetContainer
import com.chronoio.chrono.presentation.widgets.SectionType
import com.chronoio.chrono.presentation.widgets.Widget
import com.chronoio.chrono.presentation.widgets.WidgetView
import com.chronoio.chrono.test_utils.MockDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class RecentlyEditedWidgetContainerTest {

    @get:Rule
    val coroutineTestRule = DefaultCoroutineTestRule()

    @Mock
    lateinit var gateway: Gateway

    @Mock
    lateinit var storelessSubscriptionContainer: StorelessSubscriptionContainer

    @Mock
    lateinit var getSpaceView: GetSpaceView

    @Mock
    lateinit var storeOfObjectTypes: StoreOfObjectTypes

    @Mock
    lateinit var logger: Logger

    @Mock
    lateinit var dateProvider: DateProvider

    @Mock
    lateinit var getDateObjectByTimestamp: GetDateObjectByTimestamp

    @Mock
    lateinit var stringResourceProvider: StringResourceProvider

    private lateinit var urlBuilder: UrlBuilder
    private lateinit var fieldParser: FieldParser

    private val config = StubConfig()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        fieldParser = FieldParserImpl(dateProvider, logger, getDateObjectByTimestamp, stringResourceProvider)
        urlBuilder = UrlBuilderImpl(gateway = gateway)
    }

    @Test
    fun `should unsubscribe when session becomes inactive`() = runTest {
        // SETUP
        val isSessionActive = MutableStateFlow(true)
        val isWidgetCollapsed = MutableStateFlow(false)

        val widget = createWidget()

        stubGetSpaceView()
        stubSubscription(emptyList())

        val container = RecentlyEditedWidgetContainer(
            widget = widget,
            storage = storelessSubscriptionContainer,
            urlBuilder = urlBuilder,
            isWidgetCollapsed = isWidgetCollapsed,
            getSpaceView = getSpaceView,
            fieldParser = fieldParser,
            storeOfObjectTypes = storeOfObjectTypes,
            isSessionActive = isSessionActive
        )

        // TESTING
        container.view.test {
            // Initial emission when active
            awaitItem()
            awaitItem()

            // Make session inactive
            isSessionActive.value = false
            advanceUntilIdle()

            // Should unsubscribe
            verifyBlocking(storelessSubscriptionContainer, times(1)) {
                unsubscribe(eq(listOf(SUBSCRIPTION_ID)))
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should unsubscribe when widget is collapsed`() = runTest {
        // SETUP
        val isSessionActive = MutableStateFlow(true)
        val isWidgetCollapsed = MutableStateFlow(false)

        val widget = createWidget()

        stubGetSpaceView()
        stubSubscription(emptyList())

        val container = RecentlyEditedWidgetContainer(
            widget = widget,
            storage = storelessSubscriptionContainer,
            urlBuilder = urlBuilder,
            isWidgetCollapsed = isWidgetCollapsed,
            getSpaceView = getSpaceView,
            fieldParser = fieldParser,
            storeOfObjectTypes = storeOfObjectTypes,
            isSessionActive = isSessionActive
        )

        // TESTING
        container.view.test {
            // Initial emission when expanded
            awaitItem()
            awaitItem()

            // Collapse the widget
            isWidgetCollapsed.value = true
            advanceUntilIdle()
            
            // Wait for collapsed view
            awaitItem()

            // Should unsubscribe when collapsed
            verifyBlocking(storelessSubscriptionContainer, times(1)) {
                unsubscribe(eq(listOf(SUBSCRIPTION_ID)))
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should not subscribe when widget starts collapsed`() = runTest {
        // SETUP
        val isSessionActive = MutableStateFlow(true)
        val isWidgetCollapsed = flowOf(true)

        val widget = createWidget()

        stubGetSpaceView()

        val container = RecentlyEditedWidgetContainer(
            widget = widget,
            storage = storelessSubscriptionContainer,
            urlBuilder = urlBuilder,
            isWidgetCollapsed = isWidgetCollapsed,
            getSpaceView = getSpaceView,
            fieldParser = fieldParser,
            storeOfObjectTypes = storeOfObjectTypes,
            isSessionActive = isSessionActive
        )

        // TESTING
        container.view.test {
            val item = awaitItem()
            
            // Should emit collapsed view without subscribing
            assert(item is WidgetView.RecentlyEdited)
            assert(!(item as WidgetView.RecentlyEdited).isExpanded)

            // Verify subscribe was never called
            verifyBlocking(storelessSubscriptionContainer, times(0)) {
                subscribe(any<StoreSearchParams>())
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should not subscribe when session starts inactive`() = runTest {
        // SETUP
        val isSessionActive = flowOf(false)
        val isWidgetCollapsed = flowOf(false)

        val widget = createWidget()

        val container = RecentlyEditedWidgetContainer(
            widget = widget,
            storage = storelessSubscriptionContainer,
            urlBuilder = urlBuilder,
            isWidgetCollapsed = isWidgetCollapsed,
            getSpaceView = getSpaceView,
            fieldParser = fieldParser,
            storeOfObjectTypes = storeOfObjectTypes,
            isSessionActive = isSessionActive
        )

        // TESTING
        container.view.test {
            // emptyFlow() completes immediately without emitting
            awaitComplete()

            // Verify subscribe was never called
            verifyBlocking(storelessSubscriptionContainer, times(0)) {
                subscribe(any<StoreSearchParams>())
            }
        }
    }

    private fun createWidget(): Widget.RecentlyEdited {
        return Widget.RecentlyEdited(
            id = MockDataFactory.randomUuid(),
            source = Widget.Source.Bundled.Recent,
            config = config,
            icon = ObjectIcon.None,
            sectionType = SectionType.RECENTLY_EDITED
        )
    }

    private fun stubGetSpaceView() {
        getSpaceView.stub {
            onBlocking {
                async(any())
            } doReturn Resultat.success(
                ObjectWrapper.Basic(
                    mapOf(
                        Relations.ID to config.spaceView,
                        Relations.CREATED_DATE to 0.0
                    )
                )
            )
        }
    }

    private fun stubSubscription(results: List<ObjectWrapper.Basic>) {
        storelessSubscriptionContainer.stub {
            on {
                subscribe(any<StoreSearchParams>())
            } doReturn flowOf(results)
        }
    }

    companion object {
        private const val SUBSCRIPTION_ID = "subscription.widget.recently_edited"
    }
}
