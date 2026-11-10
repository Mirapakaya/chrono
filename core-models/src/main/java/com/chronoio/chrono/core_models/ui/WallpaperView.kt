package com.chronoio.chrono.core_models.ui

import com.chronoio.chrono.core_models.SystemColor

sealed class WallpaperView {

    abstract val isSelected: Boolean

    data class SpaceColor(
        override val isSelected: Boolean,
        val systemColor: SystemColor?
    ) : WallpaperView()

    data class SolidColor(
        override val isSelected: Boolean,
        val code: String,
    ) : WallpaperView()

    data class Gradient(
        override val isSelected: Boolean,
        val code: String
    ) : WallpaperView()

    companion object {
        const val WALLPAPER_DEFAULT_ALPHA = 85
    }
}