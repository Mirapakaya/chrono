package com.chronoio.chrono.presentation.sets.viewer

import com.chronoio.chrono.core_models.DVViewerType
import com.chronoio.chrono.core_models.Id

data class ViewerView(
    val id: Id,
    val name: String,
    val type: DVViewerType,
    val isActive: Boolean,
    val showActionMenu: Boolean = false,
    val isUnsupported: Boolean = false,
    val defaultObjectType: Id?,
    val relations: List<Id>,
    val sorts: List<Id>,
    val filters: List<Id>,
    val isDefaultObjectTypeEnabled: Boolean
)


