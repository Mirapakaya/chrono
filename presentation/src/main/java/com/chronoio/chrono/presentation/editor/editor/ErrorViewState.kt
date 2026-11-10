package com.chronoio.chrono.presentation.editor.editor

sealed class ErrorViewState {
    data class Toast(val msg: String) : ErrorViewState()
    object AlertDialog : ErrorViewState()
}