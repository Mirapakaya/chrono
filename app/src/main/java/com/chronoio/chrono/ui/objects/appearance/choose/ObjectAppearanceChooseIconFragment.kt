package com.chronoio.chrono.ui.objects.appearance.choose

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.presentation.objects.appearance.choose.ObjectAppearanceChooseIconViewModel
import com.chronoio.chrono.presentation.objects.appearance.choose.ObjectAppearanceChooseSettingsView
import javax.inject.Inject

class ObjectAppearanceChooseIconFragment :
    ObjectAppearanceChooseFragmentBase<ObjectAppearanceChooseSettingsView.Icon, ObjectAppearanceChooseIconViewModel>() {


    @Inject
    lateinit var factory: ObjectAppearanceChooseIconViewModel.Factory
    override val vm by viewModels<ObjectAppearanceChooseIconViewModel> { factory }
    override val title: Int = R.string.icon

    override fun injectDependencies() {
        componentManager()
            .objectAppearanceIconComponent
            .get(
                DefaultComponentParam(
                    ctx = ctx,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectAppearanceIconComponent.release()
    }

    companion object {
        fun new(ctx: Id, space: Id, block: Id) = ObjectAppearanceChooseIconFragment().apply {
            arguments = bundleOf(
                CONTEXT_ID_KEY to ctx,
                SPACE_KEY to space,
                BLOCK_ID_KEY to block
            )
        }
    }
}