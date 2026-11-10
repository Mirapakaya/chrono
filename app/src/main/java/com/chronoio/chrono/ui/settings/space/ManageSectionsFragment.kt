package com.chronoio.chrono.ui.settings.space

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.spaces.ManageSectionsComponent
import com.chronoio.chrono.presentation.spaces.ManageSectionsViewModel
import com.chronoio.chrono.ui_settings.sections.ManageSectionsScreen
import javax.inject.Inject

class ManageSectionsFragment : Fragment() {

    @Inject
    lateinit var factory: ManageSectionsViewModel.Factory

    private val vm by viewModels<ManageSectionsViewModel> { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseDependencies()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        ManageSectionsScreen(
            state = vm.uiState.collectAsStateWithLifecycle().value,
            onSectionVisibilityChanged = { sectionType, isVisible ->
                vm.onSectionVisibilityChanged(sectionType, isVisible)
            },
            onSectionsReordered = { reorderedSections ->
                vm.onSectionsReordered(reorderedSections)
            },
            onBackPressed = {
                vm.onBackPressed()
            }
        )

        LaunchedEffect(Unit) {
            vm.commands.collect { command ->
                when (command) {
                    ManageSectionsViewModel.Command.Dismiss -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun injectDependencies() {
        componentManager()
            .manageSectionsComponent
            .get()
            .inject(this)
    }

    private fun releaseDependencies() {
        componentManager()
            .manageSectionsComponent
            .release()
    }
}
