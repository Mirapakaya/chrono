package com.chronoio.chrono.domain.types

import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.primitives.TypeId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class SetPinnedObjectTypes @Inject constructor(
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetPinnedObjectTypes.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.setPinnedObjectTypes(
            space = params.space,
            types = params.types
        )
    }

    class Params(
        val space: SpaceId,
        val types: List<TypeId>
    )
}