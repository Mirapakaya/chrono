package com.chronoio.chrono.presentation.widgets.collection

sealed class InteractionMode {
    object Edit : InteractionMode()
    object View : InteractionMode()
}