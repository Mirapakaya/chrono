package com.chronoio.chrono.presentation.editor.editor.slash

import com.chronoio.chrono.core_utils.diff.DefaultObjectDiffIdentifier
import com.chronoio.chrono.presentation.relations.ObjectRelationView

sealed class SlashPropertyView : DefaultObjectDiffIdentifier {
    data class Item(val view: ObjectRelationView) : SlashPropertyView() {
        override val identifier: String get() = view.identifier
    }

    sealed class Section : SlashPropertyView() {
        object Subheader : Section() {
            override val identifier: String get() = javaClass.simpleName
        }

        object SubheaderWithBack : Section() {
            override val identifier: String get() = javaClass.simpleName
        }
    }
    object PropertyNew : SlashPropertyView() {
        override val identifier: String get() = javaClass.simpleName
    }
}