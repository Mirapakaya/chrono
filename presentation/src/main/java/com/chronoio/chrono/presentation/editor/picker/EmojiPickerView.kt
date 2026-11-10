package com.chronoio.chrono.presentation.editor.picker

import com.chronoio.chrono.core_utils.ui.ViewType

sealed class EmojiPickerView : ViewType {

    /**
     * @property page emoji's page (emoji category)
     * @property index emoji's index on the [page]
     * @property unicode emoji's char
     */
    data class Emoji(
        val unicode: String,
        val page: Int,
        val index: Int
    ) : EmojiPickerView() {
        override fun getViewType() = HOLDER_EMOJI_ITEM
    }

    /**
     * @property category emoji category
     */
    data class GroupHeader(
        val category: Int
    ) : EmojiPickerView() {
        override fun getViewType() = HOLDER_EMOJI_CATEGORY_HEADER
    }

    companion object {
        const val HOLDER_EMOJI_CATEGORY_HEADER = 1
        const val HOLDER_EMOJI_ITEM = 2
    }
}