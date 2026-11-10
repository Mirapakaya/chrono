package com.chronoio.chrono.features.sets.filter

import com.chronoio.chrono.presentation.sets.filter.FilterViewModel
import com.chronoio.chrono.ui.sets.modals.filter.ModifyFilterFromSelectedValueFragment

class TestModifyFilterFromSelectedValueFragment : ModifyFilterFromSelectedValueFragment() {

    init {
        factory = testVmFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testVmFactory: FilterViewModel.Factory
    }
}