package com.chronoio.chrono.ui.editor.modals

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_utils.ext.arg
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.domain.icon.TextBlockTarget
import com.chronoio.chrono.presentation.editor.picker.TextBlockIconPickerViewModelFactory
import com.chronoio.chrono.presentation.picker.IconPickerViewModel
import javax.inject.Inject

class TextBlockIconPickerFragment : IconPickerFragmentBase<TextBlockTarget>() {

    @Inject
    lateinit var factory: TextBlockIconPickerViewModelFactory
    override val vm by viewModels<IconPickerViewModel<TextBlockTarget>> { factory }

    private val blockId: Id
        get() = arg(ARG_BLOCK_ID_KEY)

    override val target: TextBlockTarget by lazy {
        TextBlockTarget(context, blockId)
    }

    override fun injectDependencies() {
        componentManager()
            .textBlockIconPickerComponent
            .get(
                params = DefaultComponentParam(
                    ctx = context,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().textBlockIconPickerComponent.release()
    }

    companion object {
        const val ARG_BLOCK_ID_KEY = "arg.picker.block.id"
        fun new(
            context: Id,
            blockId: Id,
            space: Id
        ) = TextBlockIconPickerFragment().apply {
            arguments = bundleOf(
                ARG_CONTEXT_ID_KEY to context,
                ARG_BLOCK_ID_KEY to blockId,
                ARG_SPACE_ID_KEY to space
            )
        }
    }
}