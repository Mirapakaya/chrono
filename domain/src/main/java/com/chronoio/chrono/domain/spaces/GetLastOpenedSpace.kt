package com.chronoio.chrono.domain.spaces

import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class GetLastOpenedSpace @Inject constructor(
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<Unit, SpaceId?>(dispatchers.io) {

    override suspend fun doWork(params: Unit): SpaceId? {
        return repo.getCurrentSpace()
    }
}