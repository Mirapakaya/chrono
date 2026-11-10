package com.chronoio.chrono.domain.wallpaper

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Wallpaper
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class GetSpaceWallpapers @Inject constructor(
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, Map<Id, Wallpaper>>(dispatchers.io) {

    override suspend fun doWork(params: Unit): Map<Id, Wallpaper> {
        return repo.getWallpapers()
    }
}