package com.chronoio.chrono.ui.objects.appearance.choose

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.presentation.objects.appearance.choose.ObjectAppearanceChoosePreviewLayoutViewModel
import com.chronoio.chrono.presentation.objects.appearance.choose.ObjectAppearanceChooseSettingsView
import javax.inject.Inject

class ObjectAppearanceChoosePreviewLayoutFragment :
    ObjectAppearanceChooseFragmentBase<ObjectAppearanceChooseSettingsView.PreviewLayout, ObjectAppearanceChoosePreviewLayoutViewModel>() {

    @Inject
    lateinit var factory: ObjectAppearanceChoosePreviewLayoutViewModel.Factory
    override val vm by viewModels<ObjectAppearanceChoosePreviewLayoutViewModel> { factory }
    override val title: Int = R.string.preview_layout

    override fun injectDependencies() {
        componentManager()
            .objectAppearancePreviewLayoutComponent
            .get(
                params = DefaultComponentParam(
                    ctx = ctx, space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectAppearancePreviewLayoutComponent.release()
    }

    companion object {
        fun new(ctx: Id, space: Id, block: Id) = ObjectAppearanceChoosePreviewLayoutFragment().apply {
            arguments = bundleOf(
                CONTEXT_ID_KEY to ctx,
                SPACE_KEY to space,
                BLOCK_ID_KEY to block
            )
        }
    }
}