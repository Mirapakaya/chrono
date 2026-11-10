package com.chronoio.chrono.features.sets.filter

import android.os.Bundle
import android.text.InputType
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import androidx.test.espresso.matcher.ViewMatchers.withText
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
import com.chronoio.chrono.ui.sets.modals.filter.ModifyFilterFromInputFieldValueFragment
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
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

@RunWith(AndroidJUnit4::class)
@LargeTest
class ModifyInputValueFilterTest {

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
    lateinit var dispatchers: AppCoroutineDispatchers

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var spaceViews: SpaceViewSubscriptionContainer

    private lateinit var updateDataViewViewer: UpdateDataViewViewer
    private lateinit var searchObjects: SearchObjects
    private lateinit var getOptions: GetOptions
    lateinit var urlBuilder: UrlBuilder

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
        urlBuilder = UrlBuilderImpl(gateway)
        TestModifyFilterFromInputFieldValueFragment.testVmFactory = FilterViewModel.Factory(
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
    fun shouldTypeTextThenClickActionButtonToApplyChanges() {

        val relationKey = MockDataFactory.randomUuid()

        // Defining viewer containing one filter

        val initialFilterText = "Foo"
        val textToType = "Bar"

        val filter = DVFilter(
            relation = relationKey,
            value = initialFilterText,
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

        val scenario = launchFragment(
            bundleOf(
                ModifyFilterFromInputFieldValueFragment.CTX_KEY to root,
                ModifyFilterFromInputFieldValueFragment.IDX_KEY to 0,
                ModifyFilterFromInputFieldValueFragment.RELATION_KEY to relationKey,
            )
        )

        // Veryfying that the initial filter text is visibile to our user

        val inputFieldInteraction = onView(withId(R.id.enterTextValueInputField))

        inputFieldInteraction.check(matches(withText(initialFilterText)))


        // Checking input type

        inputFieldInteraction.check(matches(withInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)))

        // Typing additional text before pressing action button

        inputFieldInteraction.perform(
            typeText(textToType)
        )

        inputFieldInteraction.perform(
            closeSoftKeyboard()
        )

        Thread.sleep(1000)

        // Clicking to apply button, in order to save filter changes

        onView(withId(R.id.btnBottomAction)).apply {
            perform(click())
        }

        // Veryfying that the appropriate request was made

        verifyBlocking(repo, times(1)) {
            updateDataViewViewer(
                context = root,
                target = dv.id,
                viewer = viewer.copy(
                    filters = listOf(filter.copy(value = initialFilterText + textToType))
                )
            )
        }
    }

    @Test
    fun shouldTypeNumberFilterTextThenClickActionButtonToApplyChanges() {

        val relationKey = MockDataFactory.randomUuid()

        // Defining viewer containing one filter

        val initialFilterText = "1"
        val textToType = "2"

        val filter = DVFilter(
            relation = relationKey,
            value = initialFilterText,
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
                ModifyFilterFromInputFieldValueFragment.CTX_KEY to root,
                ModifyFilterFromInputFieldValueFragment.IDX_KEY to 0,
                ModifyFilterFromInputFieldValueFragment.RELATION_KEY to relationKey,
            )
        )

        // Veryfying that the initial filter text is visibile to our user

        val inputFieldInteraction = onView(withId(R.id.enterTextValueInputField))

        inputFieldInteraction.check(matches(withText(initialFilterText)))

        // Checking input type

        inputFieldInteraction.check(
            matches(
                withInputType(
                    InputType.TYPE_CLASS_NUMBER
                            or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
                )
            )
        )

        // Typing additional text before pressing action button

        inputFieldInteraction.perform(
            typeText(textToType)
        )

        inputFieldInteraction.perform(
            closeSoftKeyboard()
        )

        // Clicking to apply button, in order to save filter changes

        Thread.sleep(1000)

        onView(withId(R.id.btnBottomAction)).apply {
            perform(click())
        }

        // Veryfying that the appropriate request was made

        val value = (initialFilterText + textToType).toDouble()
        verifyBlocking(repo, times(1)) {
            updateDataViewViewer(
                context = root,
                target = dv.id,
                viewer = viewer.copy(
                    filters = listOf(filter.copy(value = value))
                )
            )
        }
    }

    private fun launchFragment(args: Bundle): FragmentScenario<TestModifyFilterFromInputFieldValueFragment> {
        return launchFragmentInContainer(
            fragmentArgs = args,
            themeResId = R.style.AppTheme
        )
    }
}