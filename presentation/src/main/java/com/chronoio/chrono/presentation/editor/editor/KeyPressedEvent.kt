package com.chronoio.chrono.presentation.editor.editor

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Id

sealed class KeyPressedEvent {

    data class OnTitleBlockEnterKeyEvent(
        val target: Id,
        val text: String,
        val range: IntRange
    ) : KeyPressedEvent()

    /**
     * Tab indents the block: the block becomes the last child of its previous sibling.
     * Shift+Tab outdents the block: the block moves out of its parent, below the parent.
     */
    data class OnTabKeyEvent(
        val target: Id,
        val text: String,
        val marks: List<Block.Content.Text.Mark>,
        val isShift: Boolean
    ) : KeyPressedEvent()
}
