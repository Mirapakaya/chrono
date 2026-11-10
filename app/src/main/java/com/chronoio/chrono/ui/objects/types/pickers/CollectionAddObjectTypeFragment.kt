package com.chronoio.chrono.ui.objects.types.pickers

import androidx.core.os.bundleOf
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_utils.ext.withParentSafe
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.presentation.objects.ObjectTypeChangeViewModel
import com.chronoio.chrono.ui.objects.BaseObjectTypeChangeFragment

/**
 * Selecting an object type to create when adding a new object to a collection.
 * Includes list types (Set, Collection) and bookmarks.
 * Excludes media types (Image, File, Video, Audio) and templates.
 */
class CollectionAddObjectTypeFragment : BaseObjectTypeChangeFragment() {

    override fun onItemClicked(item: ObjectWrapper.Type) {
        withParentSafe<CollectionObjectTypeSelectionListener> {
            onSelectObjectTypeForCollection(objType = item)
        }
    }

    override fun resolveTitle(): String = getString(R.string.change_type)

    override fun injectDependencies() {
        val params = ObjectTypeChangeViewModel.VmParams(
            spaceId = SpaceId(space),
            screen = ObjectTypeChangeViewModel.Screen.CREATE_OBJECT_FOR_COLLECTION,
            excludeTypes = excludeTypes
        )
        componentManager().objectTypeChangeComponent.get(params).inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectTypeChangeComponent.release()
    }

    companion object {
        fun newInstance(space: Id) = CollectionAddObjectTypeFragment().apply {
            arguments = bundleOf(
                ARG_SPACE to space
            )
        }
    }
}
