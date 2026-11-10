package com.chronoio.chrono.ui.sets.modals.filter

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.presentation.sets.model.SimpleRelationView

interface CreateFilterFlow {
    fun onRelationSelected(ctx: Id, relation: SimpleRelationView)
    fun onFilterCreated()
}