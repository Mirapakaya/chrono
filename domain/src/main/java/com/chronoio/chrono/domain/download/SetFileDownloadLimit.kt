package com.chronoio.chrono.domain.download

import com.chronoio.chrono.core_models.FileDownloadLimit
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class SetFileDownloadLimit @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<FileDownloadLimit, Unit>(dispatchers.io) {

    override suspend fun doWork(params: FileDownloadLimit) {
        settings.setFileDownloadLimit(params)
    }
}
