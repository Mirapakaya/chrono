package com.chronoio.chrono.features.sets.filter

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.chronoio.chrono.R
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Relation
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
import com.chronoio.chrono.ui.sets.modals.filter.ModifyFilterFromSelectedValueFragment
import com.chronoio.chrono.utils.CoroutinesTestRule
import com.bartoszlipinski.disableanimationsrule.DisableAnimationsRule
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
@LargeTest
class ModifyTagFilterTest {

    @get:Rule
    val animationsRule = DisableAnimationsRule()

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Mock
    lateinit var dispatchers: AppCoroutineDispatchers

    @Mock
    lateinit var repo: BlockRepository

    @Mock
    lateinit var gateway: Gateway

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var spaceManager: SpaceManager

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
        TestModifyFilterFromSelectedValueFragment.testVmFactory = FilterViewModel.Factory(
            objectState = state,
            updateDataViewViewer = updateDataViewViewer,
            dispatcher = dispatcher,
            searchObjects = searchObjects,
            urlBuilder = urlBuilder,
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
    fun tagSelectionTest1() {

        val relationKey = MockDataFactory.randomUuid()
        val target = MockDataFactory.randomUuid()

        // Defining three different tags:

        val option1 = Relation.Option(
            id = MockDataFactory.randomUuid(),
            text = "Architect",
            color = MockDataFactory.randomString()
        )

        val option2 = Relation.Option(
            id = MockDataFactory.randomUuid(),
            text = "Manager",
            color = MockDataFactory.randomString()
        )

        val option3 = Relation.Option(
            id = MockDataFactory.randomUuid(),
            text = "Developer",
            color = MockDataFactory.randomString()
        )

        // Defining viewer containing one filter

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = listOf(
                DVFilter(
                    relation = relationKey,
                    value = listOf(option1.id),
                    condition = DVFilterCondition.ALL_IN
                )
            ),
            sorts = emptyList(),
            viewerRelations = listOf(
                Block.Content.DataView.Viewer.ViewerRelation(
                    key = relationKey,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.values().random()
        )

        state.value = ObjectState.DataView.Set(
            root = root,
            blocks = listOf(
                Block(
                    id = MockDataFactory.randomUuid(),
                    children = emptyList(),
                    fields = Block.Fields.empty(),
                    content = Block.Content.DataView(
                        viewers = listOf(viewer),

                        )
                )
            )
        )

        // Launching fragment

        launchFragment(
            bundleOf(
                ModifyFilterFromSelectedValueFragment.CTX_KEY to root,
                ModifyFilterFromSelectedValueFragment.IDX_KEY to 0,
                ModifyFilterFromSelectedValueFragment.RELATION_KEY to relationKey,
            )
        )

        // TESTING

        val rvMatcher = TestUtils.withRecyclerView(R.id.rvViewerFilterRecycler)

        // Checking names

        onView(rvMatcher.atPositionOnView(0, R.id.tvTagName)).apply {
            check(matches(withText(option1.text)))
        }

        onView(rvMatcher.atPositionOnView(1, R.id.tvTagName)).apply {
            check(matches(withText(option2.text)))
        }

        onView(rvMatcher.atPositionOnView(2, R.id.tvTagName)).apply {
            check(matches(withText(option3.text)))
        }

        // Veryfing that only the first tag is selected

        onView(rvMatcher.atPositionOnView(0, R.id.ivSelectTagIcon)).apply {
            check(matches(isSelected()))
        }

        onView(rvMatcher.atPositionOnView(1, R.id.ivSelectTagIcon)).apply {
            check(matches(not(isSelected())))
        }

        onView(rvMatcher.atPositionOnView(2, R.id.ivSelectTagIcon)).apply {
            check(matches(not(isSelected())))
        }

        // Verifying that the selection counter is equal to 1

        onView(withId(R.id.btnAdd)).apply {
            check(matches(withText("1")))
        }

        // Performing click, in order to sellect second status

        onView(rvMatcher.atPositionOnView(1, R.id.ivSelectTagIcon)).apply {
            perform(click())
        }

        // Veryfing that only the first tag and the second tag are selected

        onView(rvMatcher.atPositionOnView(0, R.id.ivSelectTagIcon)).apply {
            check(matches(isSelected()))
        }

        onView(rvMatcher.atPositionOnView(1, R.id.ivSelectTagIcon)).apply {
            check(matches((isSelected())))
        }

        onView(rvMatcher.atPositionOnView(2, R.id.ivSelectTagIcon)).apply {
            check(matches(not(isSelected())))
        }

        // Verifying that the selection counter is now equal to 2

        onView(withId(R.id.btnAdd)).apply {
            check(matches(withText("2")))
        }
    }

    private fun launchFragment(args: Bundle): FragmentScenario<TestModifyFilterFromSelectedValueFragment> {
        return launchFragmentInContainer(
            fragmentArgs = args,
            themeResId = R.style.AppTheme
        )
    }
}