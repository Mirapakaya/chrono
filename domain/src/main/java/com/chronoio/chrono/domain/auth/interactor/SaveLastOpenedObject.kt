package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.config.UserSettingsRepository

/**
 * Use case for saving last open object's id for restoring user session.
 * @see GetLastOpenedObject
 * @see ClearLastOpenedObject
 */
class SaveLastOpenedObject(
    private val repo: UserSettingsRepository
) : BaseUseCase<Unit, SaveLastOpenedObject.Params>() {


    override suspend fun run(params: Params) = safe {
        repo.setLastOpenedObject(
            id = params.obj,
            space = params.space
        )
    }

    data class Params(val obj: Id, val space: SpaceId)
}