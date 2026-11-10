package com.chronoio.chrono.presentation.editor.editor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.page.CloseObject
import com.chronoio.chrono.presentation.MockTypicalDocumentFactory
import com.chronoio.chrono.presentation.util.DefaultCoroutineTestRule
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

class EditorBackButtonTest : EditorPresentationTestSetup() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = DefaultCoroutineTestRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        proceedWithDefaultBeforeTestStubbing()
    }

    @Test
    fun `should proceed to the exit when the system back button is pressed`() = runTest {

        val doc = MockTypicalDocumentFactory.page(root)

        stubInterceptEvents()
        stubOpenDocument(document = doc)
        val vm = buildViewModel()

        vm.onStart(id = root, space = defaultSpace)

        advanceUntilIdle()

        // TESTING

        val stateBefore = vm.controlPanelViewState.value

        assertNotNull(stateBefore)
        assertFalse(stateBefore.styleTextToolbar.isVisible)

        vm.onSystemBackPressed(editorHasChildrenScreens = false)

        advanceUntilIdle()

        val stateBackPressed = vm.controlPanelViewState.value

        assertTrue(stateBackPressed?.styleTextToolbar?.isVisible == false)

        verifyBlocking(closePage, times(1)) { async(CloseObject.Params(root, SpaceId(defaultSpace))) }
    }
}