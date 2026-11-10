package com.chronoio.chrono.features.sets.filter

import com.chronoio.chrono.presentation.sets.filter.ViewerFilterViewModel
import com.chronoio.chrono.ui.sets.ViewerFilterFragment

class TestViewerFilterFragment : ViewerFilterFragment() {

    init {
        factory = testVmFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testVmFactory: ViewerFilterViewModel.Factory
    }
}