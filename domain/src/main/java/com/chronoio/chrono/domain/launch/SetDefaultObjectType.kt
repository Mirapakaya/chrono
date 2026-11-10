package com.chronoio.chrono.domain.launch

import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.primitives.TypeId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class SetDefaultObjectType @Inject constructor(
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetDefaultObjectType.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.setDefaultObjectType(
            space = params.space,
            type = params.type
        )
    }

    data class Params(
        val space: SpaceId,
        val type: TypeId,
    )
}