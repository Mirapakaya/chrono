package com.chronoio.chrono.library_page_icon_picker_widget

import com.chronoio.chrono.library_page_icon_picker_widget.model.ObjectIconPickerViewDiffUtil
import com.chronoio.chrono.presentation.editor.picker.EmojiPickerView
import org.junit.Test
import kotlin.test.assertEquals

class DocumentEmojiIconPickerViewDiffUtilTest {

    @Test
    fun `two emoji items should be considered the same`() {

        val page = 5
        val index = 5

        val old = listOf(
            EmojiPickerView.Emoji(
                unicode = "U+13131",
                page = page,
                index = index
            )
        )

        val new = listOf(
            EmojiPickerView.Emoji(
                unicode = "U+13131",
                page = page,
                index = index
            )
        )

        val util = ObjectIconPickerViewDiffUtil(
            old = old,
            new = new
        )

        val result = util.areItemsTheSame(0, 0)

        assertEquals(
            expected = true,
            actual = result
        )
    }
}