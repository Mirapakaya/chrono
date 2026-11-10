package com.chronoio.chrono.ui.sets.modals.filter

import com.chronoio.chrono.presentation.sets.model.Viewer

interface UpdateConditionActionReceiver {
    fun update(condition: Viewer.Filter.Condition)
}