package com.chronoio.chrono.features.sets.filter

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.chronoio.chrono.R
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.Gateway
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.misc.UrlBuilderImpl
import com.chronoio.chrono.domain.objects.DefaultObjectStore
import com.chronoio.chrono.domain.objects.DefaultStoreOfObjectTypes
import com.chronoio.chrono.domain.objects.DefaultStoreOfRelations
import com.chronoio.chrono.domain.objects.ObjectStore
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.objects.options.GetOptions
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.sets.ObjectSetDatabase
import com.chronoio.chrono.presentation.sets.filter.FilterViewModel
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.test_utils.MockDataFactory
import com.chronoio.chrono.test_utils.utils.TestUtils
import com.chronoio.chrono.test_utils.utils.checkHasText
import com.chronoio.chrono.test_utils.utils.checkIsDisplayed
import com.chronoio.chrono.test_utils.utils.checkIsNotDisplayed
import com.chronoio.chrono.test_utils.utils.performClick
import com.chronoio.chrono.ui.sets.modals.filter.CreateFilterFromSelectedValueFragment
import com.chronoio.chrono.utils.CoroutinesTestRule
import com.bartoszlipinski.disableanimationsrule.DisableAnimationsRule
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
@LargeTest
class CreateSelectedFilterTest {

    @get:Rule
    val animationsRule = DisableAnimationsRule()

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Mock
    lateinit var repo: BlockRepository

    @Mock
    lateinit var gateway: Gateway

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var dispatchers: AppCoroutineDispatchers

    @Mock
    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var spaceViews: SpaceViewSubscriptionContainer

    private lateinit var updateDataViewViewer: UpdateDataViewViewer
    private lateinit var searchObjects: SearchObjects
    private lateinit var getOptions: GetOptions
    private lateinit var urlBuilder: UrlBuilder

    private val root = MockDataFactory.randomUuid()
    private val state: MutableStateFlow<ObjectState> = MutableStateFlow(ObjectState.Init)
    private val dispatcher = Dispatcher.Default<Payload>()
    private val storeOfObjectTypes: StoreOfObjectTypes = DefaultStoreOfObjectTypes()
    private val storeOfRelations: StoreOfRelations = DefaultStoreOfRelations()
    private val objectStore: ObjectStore = DefaultObjectStore()
    private val db = ObjectSetDatabase(store = objectStore)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        updateDataViewViewer = UpdateDataViewViewer(repo, dispatchers)
        searchObjects = SearchObjects(repo)
        getOptions = GetOptions(repo)
        urlBuilder = UrlBuilderImpl(gateway)
        TestCreateSelectedFilterFragment.testVmFactory = FilterViewModel.Factory(
            updateDataViewViewer = updateDataViewViewer,
            dispatcher = dispatcher,
            urlBuilder = urlBuilder,
            searchObjects = searchObjects,
            objectState = state,
            analytics = analytics,
            storeOfObjectTypes = storeOfObjectTypes,
            storeOfRelations = storeOfRelations,
            objectSetDatabase = db,
            getOptions = getOptions,
            spaceManager = spaceManager,
            fieldParser = fieldParser,
            spaceViews = spaceViews,
            analyticSpaceHelperDelegate = mock()
        )
    }

    @Test
    fun shouldShowNonSelectedCheckedAndSelectedNotChecked() {
        val relationKey = MockDataFactory.randomUuid()

        // Defining viewer containing one filter

        val filter = DVFilter(
            relation = relationKey,
            value = true,
            operator = Block.Content.DataView.Filter.Operator.NO,
            condition = DVFilterCondition.EQUAL
        )

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = listOf(filter),
            sorts = emptyList(),
            viewerRelations = listOf(
                Block.Content.DataView.Viewer.ViewerRelation(
                    key = relationKey,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.values().random()
        )

        val dv = Block(
            id = MockDataFactory.randomUuid(),
            children = emptyList(),
            fields = Block.Fields.empty(),
            content = Block.Content.DataView(
                viewers = listOf(viewer),

                )
        )

        state.value = ObjectState.DataView.Set(
            root = root,
            blocks = listOf(dv)
        )

        // Launching fragment

        launchFragment(
            bundleOf(
                CreateFilterFromSelectedValueFragment.CTX_KEY to root,
                CreateFilterFromSelectedValueFragment.RELATION_KEY to relationKey
            )
        )

        val rvMatcher = TestUtils.withRecyclerView(R.id.rvViewerFilterRecycler)

        onView(rvMatcher.atPositionOnView(0, R.id.tvCheckbox)).apply {
            checkHasText(R.string.dv_filter_checkbox_checked)
        }
        onView(rvMatcher.atPositionOnView(0, R.id.iconChecked)).apply {
            checkIsNotDisplayed()
        }

        onView(rvMatcher.atPositionOnView(1, R.id.tvCheckbox)).apply {
            checkHasText(R.string.dv_filter_checkbox_not_checked)
        }
        onView(rvMatcher.atPositionOnView(1, R.id.iconChecked)).apply {
            checkIsDisplayed()
        }
    }

    @Test
    fun shouldChangeStateFromNotCheckedToChecked() {
        val relationKey = MockDataFactory.randomUuid()

        val target = MockDataFactory.randomUuid()

        // Defining viewer containing one filter

        val filter = DVFilter(
            relation = relationKey,
            value = true,
            operator = Block.Content.DataView.Filter.Operator.NO,
            condition = DVFilterCondition.EQUAL
        )

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = listOf(filter),
            sorts = emptyList(),
            viewerRelations = listOf(
                Block.Content.DataView.Viewer.ViewerRelation(
                    key = relationKey,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.values().random()
        )

        val dv = Block(
            id = MockDataFactory.randomUuid(),
            children = emptyList(),
            fields = Block.Fields.empty(),
            content = Block.Content.DataView(
                viewers = listOf(viewer),

                )
        )

        state.value = ObjectState.DataView.Set(
            root = root,
            blocks = listOf(dv)
        )

        // Launching fragment

        launchFragment(
            bundleOf(
                CreateFilterFromSelectedValueFragment.CTX_KEY to root,
                CreateFilterFromSelectedValueFragment.RELATION_KEY to relationKey
            )
        )

        val rvMatcher = TestUtils.withRecyclerView(R.id.rvViewerFilterRecycler)

        onView(rvMatcher.atPositionOnView(0, R.id.tvCheckbox)).performClick()

        onView(rvMatcher.atPositionOnView(0, R.id.iconChecked)).apply {
            checkIsDisplayed()
        }
        onView(rvMatcher.atPositionOnView(1, R.id.iconChecked)).apply {
            checkIsNotDisplayed()
        }

        onView(rvMatcher.atPositionOnView(1, R.id.tvCheckbox)).performClick()

        onView(rvMatcher.atPositionOnView(0, R.id.iconChecked)).apply {
            checkIsNotDisplayed()
        }
        onView(rvMatcher.atPositionOnView(1, R.id.iconChecked)).apply {
            checkIsDisplayed()
        }
    }

    private fun launchFragment(args: Bundle): FragmentScenario<TestCreateSelectedFilterFragment> {
        return launchFragmentInContainer(
            fragmentArgs = args,
            themeResId = R.style.AppTheme
        )
    }
}