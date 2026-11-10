package com.chronoio.chrono.features.sets.sort

import com.chronoio.chrono.presentation.sets.sort.ViewerSortViewModel
import com.chronoio.chrono.ui.sets.modals.sort.ViewerSortFragment

class TestViewerSortFragment : ViewerSortFragment() {

    init {
        factory = testVmFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testVmFactory: ViewerSortViewModel.Factory
    }
}