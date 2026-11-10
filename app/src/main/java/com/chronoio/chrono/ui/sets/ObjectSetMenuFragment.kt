package com.chronoio.chrono.ui.sets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_utils.ext.subscribe
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.presentation.objects.menu.ObjectSetMenuViewModel
import com.chronoio.chrono.ui.editor.sheets.ObjectMenuBaseFragment
import javax.inject.Inject

class ObjectSetMenuFragment : ObjectMenuBaseFragment() {

    @Inject
    lateinit var factory: ObjectSetMenuViewModel.Factory
    override val vm by viewModels<ObjectSetMenuViewModel> { factory }

    override fun onStart() {
        super.onStart()
        with(lifecycleScope) {
            subscribe(vm.isObjectArchived) { isArchived ->
                if (isArchived) {
                    findNavController().popBackStack(R.id.objectSetScreen, true)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.publishToWeb.visibility = View.GONE
        binding.publishToWebDivider.visibility = View.GONE
    }

    override fun injectDependencies() {
        componentManager()
            .objectSetMenuComponent
            .get(
                params = DefaultComponentParam(
                    ctx = ctx,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectSetMenuComponent.release()
    }
}