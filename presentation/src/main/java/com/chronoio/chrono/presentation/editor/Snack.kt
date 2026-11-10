package com.chronoio.chrono.presentation.editor

import com.chronoio.chrono.core_models.Id

sealed class Snack {
    data class UndoRedo(val message: String) : Snack()
}