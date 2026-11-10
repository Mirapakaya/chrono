package com.chronoio.chrono.ui.editor.sheets

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_utils.ext.gone
import com.chronoio.chrono.core_utils.ext.subscribe
import com.chronoio.chrono.core_utils.ext.visible
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.presentation.objects.menu.ObjectMenuViewModel
import javax.inject.Inject

class ObjectMenuFragment : ObjectMenuBaseFragment() {

    @Inject
    lateinit var factory: ObjectMenuViewModel.Factory
    override val vm by viewModels<ObjectMenuViewModel> { factory }

    override fun onStart() {
        super.onStart()
        with(lifecycleScope) {
            subscribe(vm.isObjectArchived) { isArchived ->
                if (isArchived) {
                    findNavController().popBackStack(R.id.pageScreen, true)
                }
            }
            subscribe(vm.canBePublished) { canBePublished ->
                if (canBePublished) {
                    binding.publishToWeb.visible()
                    binding.publishToWebDivider.visible()
                } else {
                    binding.publishToWeb.gone()
                    binding.publishToWebDivider.gone()
                }
            }
        }

        vm.onResolveWebPublishPermission(space = SpaceId(space))
    }

    override fun injectDependencies() {
        componentManager()
            .objectMenuComponent
            .get(
                params = DefaultComponentParam(
                    ctx = ctx,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectMenuComponent.release()
    }

    companion object {
        fun args(
            ctx: Id,
            space: Id,
            isArchived: Boolean,
            isFavorite: Boolean,
            isLocked: Boolean,
            fromName: String?,
            isTemplate: Boolean,
            isReadOnly: Boolean,
            isQuickCapture: Boolean = false
        ) = bundleOf(
            CTX_KEY to ctx,
            SPACE_KEY to space,
            IS_ARCHIVED_KEY to isArchived,
            IS_FAVORITE_KEY to isFavorite,
            IS_LOCKED_KEY to isLocked,
            FROM_NAME to fromName,
            IS_TEMPLATE_KEY to isTemplate,
            IS_READ_ONLY_KEY to isReadOnly,
            IS_QUICK_CAPTURE_KEY to isQuickCapture
        )
    }
}