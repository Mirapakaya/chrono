package com.chronoio.chrono.domain.wallpaper

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Wallpaper
import com.chronoio.chrono.core_models.Wallpaper.Color
import com.chronoio.chrono.core_models.Wallpaper.Gradient
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class SetWallpaper @Inject constructor(
    private val repo: UserSettingsRepository,
    private val store: WallpaperStore,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetWallpaper.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        when (params) {
            is Params.Gradient -> {
                repo.setWallpaper(
                    space = params.space,
                    wallpaper = Gradient(params.code)
                )
                store.set(Gradient(params.code))
            }

            is Params.SolidColor -> {
                repo.setWallpaper(
                    space = params.space,
                    wallpaper = Color(params.code)
                )
                store.set(Color(params.code))
            }

            is Params.Clear -> {
                repo.setWallpaper(
                    space = params.space,
                    wallpaper = Wallpaper.Default
                )
                store.set(Wallpaper.Default)
            }
        }
    }

    sealed class Params {
        data class Clear(val space: Id) : Params()
        data class SolidColor(val space: Id, val code: String) : Params()
        data class Gradient(val space: Id, val code: String) : Params()
    }
}