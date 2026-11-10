package com.chronoio.chrono.core_ui.features.editor.scrollandmove

import com.chronoio.chrono.presentation.editor.editor.sam.ScrollAndMoveTarget
import com.chronoio.chrono.presentation.editor.editor.sam.ScrollAndMoveTargetDescriptor


class DefaultScrollAndMoveTargetDescriptor : ScrollAndMoveTargetDescriptor {

    var target: ScrollAndMoveTarget? = null

    override fun clear() {
        target = null
    }

    override fun current(): ScrollAndMoveTarget? = target

    override fun update(target: ScrollAndMoveTarget) {
        this.target = target
    }
}