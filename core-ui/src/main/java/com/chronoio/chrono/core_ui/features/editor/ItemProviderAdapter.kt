package com.chronoio.chrono.core_ui.features.editor

interface ItemProviderAdapter<T> {
    fun provide(pos: Int): T
}
