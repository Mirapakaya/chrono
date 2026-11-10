package com.chronoio.chrono.presentation.objects

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.restrictions.ObjectRestriction
import com.chronoio.chrono.presentation.editor.Editor

interface LockedStateProvider {
    fun isLocked(ctx: Id) : Boolean
    fun isContainsDetailsRestriction() : Boolean
    class EditorLockedStateProvider(
        private val storage: Editor.Storage
    ) : LockedStateProvider {
        override fun isLocked(ctx: Id): Boolean {
            val doc = storage.document.get().find { it.id == ctx }
            return doc?.fields?.isLocked ?: false
        }

        override fun isContainsDetailsRestriction(): Boolean {
            return storage.objectRestrictions.current().contains(ObjectRestriction.DETAILS)
        }
    }
    object DataViewLockedStateProvider : LockedStateProvider {
        // Sets or Collections can't be locked currently.
        override fun isLocked(ctx: Id): Boolean = false
        override fun isContainsDetailsRestriction(): Boolean = false
    }
}