package com.chronoio.chrono.presentation.editor.editor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.ext.content
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.interactor.UpdateCheckbox
import com.chronoio.chrono.presentation.editor.editor.model.BlockView
import com.chronoio.chrono.presentation.util.DefaultCoroutineTestRule
import com.chronoio.chrono.test_utils.MockDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

class EditorCheckboxTest : EditorPresentationTestSetup() {

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
    fun `should start updating checkbox as checked when it is clicked`() = runTest {

        // SETUP

        val child = MockDataFactory.randomUuid()

        val checkbox = Block(
            id = child,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Text(
                text = MockDataFactory.randomString(),
                isChecked = false,
                marks = emptyList(),
                style = Block.Content.Text.Style.CHECKBOX
            ),
            children = emptyList()
        )

        val view = BlockView.Text.Checkbox(
            id = checkbox.id,
            text = checkbox.content<Block.Content.Text>().text,
            isChecked = true
        )

        val page = listOf(
            Block(
                id = root,
                fields = Block.Fields(emptyMap()),
                content = Block.Content.Smart,
                children = listOf(child)
            ),
            checkbox
        )

        stubInterceptEvents()
        stubOpenDocument(document = page)
        stubUpdateCheckbox()

        val vm = buildViewModel()

        // TESTING

        vm.onStart(id = root, space = defaultSpace)

        advanceUntilIdle()

        vm.onCheckboxClicked(view)

        advanceUntilIdle()

        verifyBlocking(updateCheckbox, times(1)) {
            invoke(
                eq(
                    UpdateCheckbox.Params(
                        context = root,
                        target = child,
                        isChecked = true
                    )
                )
            )
        }
    }

    @Test
    fun `should start updating checkbox as not checked when it is clicked`() = runTest {

        // SETUP

        val child = MockDataFactory.randomUuid()

        val checkbox = Block(
            id = child,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Text(
                text = MockDataFactory.randomString(),
                isChecked = true,
                marks = emptyList(),
                style = Block.Content.Text.Style.CHECKBOX
            ),
            children = emptyList()
        )

        val view = BlockView.Text.Checkbox(
            id = checkbox.id,
            text = checkbox.content<Block.Content.Text>().text,
            isChecked = false
        )

        val page = listOf(
            Block(
                id = root,
                fields = Block.Fields(emptyMap()),
                content = Block.Content.Smart,
                children = listOf(child)
            ),
            checkbox
        )

        stubInterceptEvents()
        stubOpenDocument(document = page)
        stubUpdateCheckbox()

        val vm = buildViewModel()

        // TESTING

        vm.onStart(id = root, space = defaultSpace)

        advanceUntilIdle()

        vm.onCheckboxClicked(view)

        advanceUntilIdle()

        verifyBlocking(updateCheckbox, times(1)) {
            invoke(
                eq(
                    UpdateCheckbox.Params(
                        context = root,
                        target = child,
                        isChecked = false
                    )
                )
            )
        }
    }

    private fun stubUpdateCheckbox(
        payload: Payload = Payload(
            context = MockDataFactory.randomUuid(),
            events = emptyList()
        )
    ) {
        updateCheckbox.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(payload)
        }
    }
}