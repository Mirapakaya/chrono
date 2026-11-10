package com.chronoio.chrono.ui.objects.appearance.choose

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.presentation.objects.appearance.choose.ObjectAppearanceChooseDescriptionViewModel
import com.chronoio.chrono.presentation.objects.appearance.choose.ObjectAppearanceChooseSettingsView
import javax.inject.Inject

class ObjectAppearanceChooseDescriptionFragment :
    ObjectAppearanceChooseFragmentBase<ObjectAppearanceChooseSettingsView.Description, ObjectAppearanceChooseDescriptionViewModel>() {

    @Inject
    lateinit var factory: ObjectAppearanceChooseDescriptionViewModel.Factory
    override val vm by viewModels<ObjectAppearanceChooseDescriptionViewModel> { factory }
    override val title: Int = R.string.description

    override fun injectDependencies() {
        componentManager()
            .objectAppearanceChooseDescriptionComponent
            .get(params = DefaultComponentParam(ctx = ctx, space = SpaceId(space)))
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectAppearanceChooseDescriptionComponent.release()
    }

    companion object {
        fun new(
            ctx: Id,
            space: Id,
            block: Id
        ) = ObjectAppearanceChooseDescriptionFragment().apply {
            arguments = bundleOf(
                CONTEXT_ID_KEY to ctx,
                SPACE_KEY to space,
                BLOCK_ID_KEY to block
            )
        }
    }
}