package com.chronoio.chrono.domain.download

import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class SetUseCellularForDownloads @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Boolean, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Boolean) {
        settings.setUseCellularForDownloads(params)
    }
}
