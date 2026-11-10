package com.chronoio.chrono.presentation.linking

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.StubParagraph
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_utils.tools.UrlValidator
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.config.Gateway
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.misc.UrlBuilderImpl
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.objects.DefaultStoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.editor.Editor
import com.chronoio.chrono.presentation.util.CoroutinesTestRule
import com.chronoio.chrono.test_utils.MockDataFactory
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub


open class LinkToObjectOrWebViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var searchObjects: SearchObjects

    @Mock
    lateinit var urlValidator: UrlValidator

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate

    @Mock
    lateinit var gateway: Gateway

    var ctx = ""
    var store: Editor.Storage = Editor.Storage()
    val spaceId = MockDataFactory.randomString()

    protected val builder: UrlBuilder get() = UrlBuilderImpl(gateway)
    private val storeOfObjectTypes: StoreOfObjectTypes = DefaultStoreOfObjectTypes()

    val vmParams = LinkToObjectOrWebViewModel.VmParams(
        space = SpaceId(spaceId),
    )


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        stubSpaceManager()
        stubAnalyticSpaceHelperDelegate()
    }

    @After
    fun after() {
        release()
    }

    @Test
    fun `should return selected block error state when block is not present in state`() = runTest {

        spaceManager.set(spaceId)

        val target = MockDataFactory.randomString()

        val block = StubParagraph()

        runBlocking { store.document.update(listOf(block)) }

        val vm = givenViewModel()
        stubSearchObjects(params = vm.getSearchObjectsParams(ctx))

        vm.onStart(
            blockId = target,
            rangeStart = 0,
            rangeEnd = 10,
            clipboardUrl = null,
            ignore = ctx
        )

        assertEquals(
            expected = LinkToObjectOrWebViewModel.ViewState.ErrorSelectedBlock,
            actual = vm.viewState.value
        )
    }

    @Test
    fun `should return selected range error state when selection range end is bigger then text length`() = runTest {

        spaceManager.set(spaceId)
        val target = MockDataFactory.randomString()

        val start = 0
        val end = 7

        val block = StubParagraph(id = target, text = "FooBar")

        runBlocking { store.document.update(listOf(block)) }

        val vm = givenViewModel()

        stubSearchObjects(params = vm.getSearchObjectsParams(ctx))

        vm.onStart(
            blockId = target,
            rangeStart = start,
            rangeEnd = end,
            clipboardUrl = null,
            ignore = ctx
        )

        val result = vm.viewState.value

        assertEquals(
            expected = LinkToObjectOrWebViewModel.ViewState.ErrorSelection,
            actual = result
        )
    }

    @Test
    fun `should return selected range error state when selection range start is equal end`() = runTest {


        spaceManager.set(spaceId)
        val target = MockDataFactory.randomString()

        val start = 3
        val end = 3

        val block = StubParagraph(id = target, text = "FooBar")

        runBlocking { store.document.update(listOf(block)) }

        val vm = givenViewModel()
        stubSearchObjects(params = vm.getSearchObjectsParams(ctx))

        vm.onStart(
            blockId = target,
            rangeStart = start,
            rangeEnd = end,
            clipboardUrl = null,
            ignore = ctx
        )

        val result = vm.viewState.value

        assertEquals(
            expected = LinkToObjectOrWebViewModel.ViewState.ErrorSelection,
            actual = result
        )
    }

    @Test
    fun `should set markup link param when present in text link type markup`() = runTest {

        spaceManager.set(spaceId)
        val target = MockDataFactory.randomUuid()
        val url = MockDataFactory.randomString()

        val block = StubParagraph(
            id = target, text = "FooBar", marks = listOf(
                Block.Content.Text.Mark(
                    range = IntRange(0, 6),
                    type = Block.Content.Text.Mark.Type.LINK,
                    param = url
                )
            )
        )

        runBlocking { store.document.update(listOf(block)) }

        val vm = givenViewModel()

        stubSearchObjects(params = vm.getSearchObjectsParams(ctx))

        vm.onStart(
            blockId = target,
            rangeStart = 0,
            rangeEnd = 6,
            clipboardUrl = null,
            ignore = ctx
        )

        val result = vm.markupLinkParam.value

        assertEquals(expected = url, actual = result)
        assertNull(vm.markupObjectParam.value)
    }

    @Test
    fun `should set markup object param when present in text object type markup`() = runTest {

        spaceManager.set(spaceId)
        val target = MockDataFactory.randomUuid()
        val url = MockDataFactory.randomString()

        val block = StubParagraph(
            id = target, text = "FooBar", marks = listOf(
                Block.Content.Text.Mark(
                    range = IntRange(2, 5),
                    type = Block.Content.Text.Mark.Type.OBJECT,
                    param = url
                )
            )
        )

        runBlocking { store.document.update(listOf(block)) }

        val vm = givenViewModel()

        stubSearchObjects(params = vm.getSearchObjectsParams(ctx))

        vm.onStart(
            blockId = target,
            rangeStart = 0,
            rangeEnd = 6,
            clipboardUrl = null,
            ignore = ctx
        )

        val result = vm.markupObjectParam.value
        assertEquals(expected = url, actual = result)
        assertNull(vm.markupLinkParam.value)
    }

    @Test
    fun `should not set object link when not present in range text markup`() = runTest {

        spaceManager.set(spaceId)
        val target = MockDataFactory.randomUuid()
        val url = MockDataFactory.randomString()

        val block = StubParagraph(
            id = target, text = "FooBarFooBar", marks = listOf(
                Block.Content.Text.Mark(
                    range = IntRange(0, 3),
                    type = Block.Content.Text.Mark.Type.OBJECT,
                    param = url
                ),
                Block.Content.Text.Mark(
                    range = IntRange(7, 12),
                    type = Block.Content.Text.Mark.Type.LINK,
                    param = url
                )
            )
        )

        runBlocking { store.document.update(listOf(block)) }

        val vm = givenViewModel()

        stubSearchObjects(params = vm.getSearchObjectsParams(ctx))

        vm.onStart(
            blockId = target,
            rangeStart = 4,
            rangeEnd = 6,
            clipboardUrl = null,
            ignore = ctx
        )

        assertNull(vm.markupObjectParam.value)
        assertNull(vm.markupLinkParam.value)
    }

    private fun release() {
        coroutineTestRule.advanceTime(LinkToObjectOrWebViewModel.SEARCH_INPUT_DEBOUNCE)
    }

    private fun stubSearchObjects(
        params: SearchObjects.Params,
        objects: List<ObjectWrapper.Basic> = emptyList()
    ) {
        searchObjects.stub {
            onBlocking { invoke(params) } doReturn Either.Right(objects)
        }
    }

    @Mock
    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var spaceViews: SpaceViewSubscriptionContainer

    private fun givenViewModel() = LinkToObjectOrWebViewModel(
        searchObjects = searchObjects,
        analytics = analytics,
        stores = store,
        urlBuilder = builder,
        urlValidator = urlValidator,
        storeOfObjectTypes = storeOfObjectTypes,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        vmParams = vmParams,
        fieldParser = fieldParser,
        spaceViews = spaceViews
    )

    fun stubSpaceManager() {
        spaceManager.stub {
            onBlocking { get() } doReturn ""
        }
    }

    private fun stubAnalyticSpaceHelperDelegate() {
        analyticSpaceHelperDelegate.stub {
            on { provideParams("") } doReturn AnalyticSpaceHelperDelegate.Params.EMPTY
        }
    }
}