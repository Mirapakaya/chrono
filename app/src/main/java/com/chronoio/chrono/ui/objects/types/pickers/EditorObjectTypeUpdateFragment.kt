package com.chronoio.chrono.ui.objects.types.pickers

import androidx.core.os.bundleOf
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_utils.ext.argOrNull
import com.chronoio.chrono.core_utils.ext.withParentSafe
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.presentation.objects.ObjectTypeChangeViewModel
import com.chronoio.chrono.ui.objects.BaseObjectTypeChangeFragment

/**
 * Fragment for changing object type in the editor.
 * Excludes: Lists (Set, Collection), Participants, Templates.
 */
class EditorObjectTypeUpdateFragment : BaseObjectTypeChangeFragment() {

    private val fromFeatured: Boolean
        get() = argOrNull<Boolean>(ARG_FROM_FEATURED) == true

    override fun onItemClicked(item: ObjectWrapper.Type) {
        withParentSafe<ObjectTypeUpdateListener> {
            onUpdateObjectType(objType = item, fromFeatured = fromFeatured)
        }
    }

    override fun resolveTitle(): String = getString(R.string.change_type)

    override fun injectDependencies() {
        val params = ObjectTypeChangeViewModel.VmParams(
            spaceId = SpaceId(space),
            screen = ObjectTypeChangeViewModel.Screen.EDITOR_OBJECT_TYPE_UPDATE,
            excludeTypes = excludeTypes
        )
        componentManager().objectTypeChangeComponent.get(params).inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectTypeChangeComponent.release()
    }

    companion object {
        private const val ARG_FROM_FEATURED = "arg.editor-object-type-update.from-featured"

        fun newInstance(
            space: Id,
            fromFeatured: Boolean = false
        ) = EditorObjectTypeUpdateFragment().apply {
            arguments = bundleOf(
                ARG_SPACE to space,
                ARG_FROM_FEATURED to fromFeatured
            )
        }
    }
}
