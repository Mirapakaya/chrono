package com.chronoio.chrono.presentation.editor.editor

sealed class SideEffect {
    object ClearMultiSelectSelection : SideEffect()
    object ResetFocusToFirstTextBlock: SideEffect()
}