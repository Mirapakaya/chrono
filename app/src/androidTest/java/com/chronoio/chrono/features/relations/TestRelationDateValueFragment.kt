package com.chronoio.chrono.features.relations

import com.chronoio.chrono.presentation.sets.RelationDateValueViewModel
import com.chronoio.chrono.ui.relations.RelationDateValueFragment

class TestRelationDateValueFragment: RelationDateValueFragment() {

    init {
        factory = testVmFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testVmFactory: RelationDateValueViewModel.Factory
    }
}