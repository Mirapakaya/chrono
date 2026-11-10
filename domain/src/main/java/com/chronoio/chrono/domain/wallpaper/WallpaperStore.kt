package com.chronoio.chrono.domain.wallpaper

import com.chronoio.chrono.core_models.Wallpaper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// TODo store data about wallpaper's space
interface WallpaperStore {
    fun set(wallpaper: Wallpaper)
    fun get() : Wallpaper
    fun observe() : Flow<Wallpaper>

    object Default : WallpaperStore {
        private val wallpaper = MutableStateFlow<Wallpaper>(Wallpaper.Default)
        override fun set(wallpaper: Wallpaper) { this.wallpaper.value = wallpaper }
        override fun get(): Wallpaper = wallpaper.value
        override fun observe(): Flow<Wallpaper> = wallpaper
    }
}