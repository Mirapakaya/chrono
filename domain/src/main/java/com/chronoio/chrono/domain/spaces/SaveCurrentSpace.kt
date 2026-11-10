package com.chronoio.chrono.domain.spaces

import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class SaveCurrentSpace @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: UserSettingsRepository
) : ResultInteractor<SaveCurrentSpace.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.setCurrentSpace(params.space)
    }

    class Params(val space: SpaceId)
}