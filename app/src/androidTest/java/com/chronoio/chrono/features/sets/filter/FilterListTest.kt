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
import com.chronoio.chrono.core_models.Relation
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.Gateway
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.misc.UrlBuilderImpl
import com.chronoio.chrono.domain.objects.DefaultObjectStore
import com.chronoio.chrono.domain.objects.DefaultStoreOfRelations
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.presentation.relations.ObjectSetConfig
import com.chronoio.chrono.presentation.sets.ObjectSetDatabase
import com.chronoio.chrono.presentation.sets.filter.ViewerFilterViewModel
import com.chronoio.chrono.presentation.sets.model.Viewer
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.test_utils.MockDataFactory
import com.chronoio.chrono.test_utils.utils.TestUtils
import com.chronoio.chrono.test_utils.utils.checkHasText
import com.chronoio.chrono.ui.sets.ViewerFilterFragment
import com.chronoio.chrono.utils.CoroutinesTestRule
import com.bartoszlipinski.disableanimationsrule.DisableAnimationsRule
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
@LargeTest
class FilterListTest {

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
    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var storeOfObjectTypes: StoreOfObjectTypes

    lateinit var updateDataViewViewer: UpdateDataViewViewer
    lateinit var searchObjects: SearchObjects
    lateinit var urlBuilder: UrlBuilder

    private val root = MockDataFactory.randomUuid()
    private val state: MutableStateFlow<ObjectState> = MutableStateFlow(ObjectState.Init)
    private val dispatcher = Dispatcher.Default<Payload>()
    private val storeOfObjects = DefaultObjectStore()
    private val db = ObjectSetDatabase(storeOfObjects)
    private val storeOfRelations = DefaultStoreOfRelations()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        updateDataViewViewer = UpdateDataViewViewer(repo, dispatchers = dispatchers)
        searchObjects = SearchObjects(repo)
        urlBuilder = UrlBuilderImpl(gateway)
        TestViewerFilterFragment.testVmFactory = ViewerFilterViewModel.Factory(
            updateDataViewViewer = updateDataViewViewer,
            dispatcher = dispatcher,
            urlBuilder = urlBuilder,
            state = state,
            analytics = analytics,
            storeOfRelations = storeOfRelations,
            objectSetDatabase = db,
            fieldParser = fieldParser,
            storeOfObjectTypes = storeOfObjectTypes
        )
    }

    @Test
    fun shouldShowCheckboxFilterWithEqualChecked() {
        val relationKey = MockDataFactory.randomUuid()

        val target = MockDataFactory.randomUuid()

        val record: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to target,
            relationKey to emptyList<String>()
        )

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

        val relation = Relation(
            key = relationKey,
            defaultValue = null,
            isHidden = false,
            isReadOnly = false,
            isMulti = true,
            name = "Is read",
            source = Relation.Source.values().random(),
            format = Relation.Format.CHECKBOX,
            selections = emptyList()
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
                ViewerFilterFragment.CONTEXT_ID_KEY to root
            )
        )

        val rvMatcher = TestUtils.withRecyclerView(R.id.recycler)

        onView(rvMatcher.atPositionOnView(0, R.id.tvTitle)).apply {
            checkHasText(relation.name)
        }

        onView(rvMatcher.atPositionOnView(0, R.id.tvCondition)).apply {
            checkHasText(Viewer.Filter.Condition.Checkbox.Equal().title)
        }

        onView(rvMatcher.atPositionOnView(0, R.id.tvValue)).apply {
            checkHasText("checked")
        }

    }

    @Test
    fun shouldShowCheckboxFilterWithNotEqualNotChecked() {
        val relationKey = MockDataFactory.randomUuid()

        val target = MockDataFactory.randomUuid()

        val record: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to target,
            relationKey to emptyList<String>()
        )

        // Defining viewer containing one filter

        val filter = DVFilter(
            relation = relationKey,
            value = false,
            operator = Block.Content.DataView.Filter.Operator.NO,
            condition = DVFilterCondition.NOT_EQUAL
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

        val relation = Relation(
            key = relationKey,
            defaultValue = null,
            isHidden = false,
            isReadOnly = false,
            isMulti = true,
            name = "Is read",
            source = Relation.Source.values().random(),
            format = Relation.Format.CHECKBOX,
            selections = emptyList()
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
                ViewerFilterFragment.CONTEXT_ID_KEY to root
            )
        )

        val rvMatcher = TestUtils.withRecyclerView(R.id.recycler)

        onView(rvMatcher.atPositionOnView(0, R.id.tvTitle)).apply {
            checkHasText(relation.name)
        }

        onView(rvMatcher.atPositionOnView(0, R.id.tvCondition)).apply {
            checkHasText(Viewer.Filter.Condition.Checkbox.NotEqual().title)
        }

        onView(rvMatcher.atPositionOnView(0, R.id.tvValue)).apply {
            checkHasText("not checked")
        }

    }

    @Test
    fun shouldShowCheckboxFilterWithEqualNotCheckedWhenValueNull() {
        val relationKey = MockDataFactory.randomUuid()

        val target = MockDataFactory.randomUuid()

        val record: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to target,
            relationKey to emptyList<String>()
        )

        // Defining viewer containing one filter

        val filter = DVFilter(
            relation = relationKey,
            value = null,
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

        val relation = Relation(
            key = relationKey,
            defaultValue = null,
            isHidden = false,
            isReadOnly = false,
            isMulti = true,
            name = "Is read",
            source = Relation.Source.values().random(),
            format = Relation.Format.CHECKBOX,
            selections = emptyList()
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
                ViewerFilterFragment.CONTEXT_ID_KEY to root
            )
        )

        val rvMatcher = TestUtils.withRecyclerView(R.id.recycler)

        onView(rvMatcher.atPositionOnView(0, R.id.tvTitle)).apply {
            checkHasText(relation.name)
        }

        onView(rvMatcher.atPositionOnView(0, R.id.tvCondition)).apply {
            checkHasText(Viewer.Filter.Condition.Checkbox.Equal().title)
        }

        onView(rvMatcher.atPositionOnView(0, R.id.tvValue)).apply {
            checkHasText("not checked")
        }

    }

    private fun launchFragment(args: Bundle): FragmentScenario<TestViewerFilterFragment> {
        return launchFragmentInContainer(
            fragmentArgs = args,
            themeResId = R.style.AppTheme
        )
    }
}