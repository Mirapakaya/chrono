package com.chronoio.chrono.core_ui.extensions

import com.chronoio.chrono.core_ui.common.isLinksOrMentionsPresent
import com.chronoio.chrono.core_ui.widgets.text.TextInputWidget
import com.chronoio.chrono.presentation.editor.editor.Markup

fun TextInputWidget.applyMovementMethod(item: Markup) {
    if (item.marks.isNotEmpty() && item.marks.isLinksOrMentionsPresent()) {
        setLinksClickable()
    } else {
        setDefaultMovementMethod()
    }
}

fun TextInputWidget.disable() {
    isEnabled = false
    pauseTextWatchers {
        enableReadMode()
    }
}