package com.chronoio.chrono.core_ui.extensions

import androidx.compose.ui.Modifier

fun Modifier.conditional(
    condition: Boolean,
    positive: Modifier.() -> Modifier,
    negative: (Modifier.() -> Modifier)? = null
): Modifier {
    return if (condition) {
        then(positive(Modifier))
    } else if (negative != null) {
        then(negative(Modifier))
    } else {
        this
    }
}