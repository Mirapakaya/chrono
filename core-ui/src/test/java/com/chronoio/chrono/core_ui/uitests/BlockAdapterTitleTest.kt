package com.chronoio.chrono.core_ui.uitests

import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.presentation.editor.editor.model.BlockView
import com.chronoio.chrono.test_utils.MockDataFactory
import com.chronoio.chrono.test_utils.TestFragment
import com.chronoio.chrono.test_utils.utils.checkIsDisplayed
import com.chronoio.chrono.test_utils.utils.checkIsFocused
import com.chronoio.chrono.test_utils.utils.checkIsNotFocused
import com.chronoio.chrono.test_utils.utils.onItemView
import com.chronoio.chrono.test_utils.utils.performClick
import com.chronoio.chrono.test_utils.utils.rVMatcher
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.chronoio.chrono.test_utils.R as R_test

@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.P],
    instrumentedPackages = [
        "androidx.loader.content"
    ]
)
class BlockAdapterTitleTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    lateinit var scenario: FragmentScenario<TestFragment>

    @Before
    fun setUp() {
        context.setTheme(R.style.Theme_MaterialComponents)
        scenario = launchFragmentInContainer()
    }


    @Test
    fun `should trigger focus event when title view gets focused`() {
        scenario.onFragment {

            var isEventTriggered = false

            val block = givenTitle()
            val adapter = givenAdapter(
                views = listOf(block),
                onFocusChanged = { id, hasFocus ->
                    if (id == block.id && hasFocus) {
                        isEventTriggered = true
                    }
                }
            )
            givenRecycler(it).apply {
                this.adapter = adapter
            }

            R_test.id.recycler.rVMatcher().apply {
                onItemView(0, R.id.title).checkIsDisplayed()
                onItemView(0, R.id.title).checkIsNotFocused()
                assertFalse { block.isFocused }
                onItemView(0, R.id.title).performClick()
                onItemView(0, R.id.title).checkIsFocused()
                assertTrue { block.isFocused }
                assertTrue { isEventTriggered }
            }
        }
    }

    private fun givenTitle() = BlockView.Title.Basic(
        text = MockDataFactory.randomString(),
        id = MockDataFactory.randomUuid(),
        isFocused = false
    )

    private fun givenRecycler(fragment: Fragment): RecyclerView {
        val recycler = fragment.view!!.findViewById<RecyclerView>(R_test.id.recycler).apply {
            layoutManager = LinearLayoutManager(context)
        }
        return recycler
    }
}