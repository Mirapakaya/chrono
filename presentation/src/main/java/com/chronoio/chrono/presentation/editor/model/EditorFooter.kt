package com.chronoio.chrono.presentation.editor.model

sealed class EditorFooter {
    object Note : EditorFooter()
    object None : EditorFooter()
}