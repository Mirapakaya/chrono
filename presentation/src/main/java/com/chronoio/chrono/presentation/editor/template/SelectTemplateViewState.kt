package com.chronoio.chrono.presentation.editor.template

import com.chronoio.chrono.core_models.Id

sealed class SelectTemplateViewState {
    object Idle : SelectTemplateViewState()
    data class Active(val count: Int, val typeId: Id) : SelectTemplateViewState()
}