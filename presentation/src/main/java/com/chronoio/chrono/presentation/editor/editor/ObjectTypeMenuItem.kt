package com.chronoio.chrono.presentation.editor.editor

sealed class ObjectTypeMenuItem {
    object ChangeType: ObjectTypeMenuItem()
    object OpenType: ObjectTypeMenuItem()
}
