package com.chronoio.chrono.features.relations

import com.chronoio.chrono.presentation.sets.RelationTextValueViewModel
import com.chronoio.chrono.ui.relations.RelationTextValueFragment

class TestRelationTextValueFragment : RelationTextValueFragment() {

    init {
        factory = testVmFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testVmFactory: RelationTextValueViewModel.Factory
    }
}