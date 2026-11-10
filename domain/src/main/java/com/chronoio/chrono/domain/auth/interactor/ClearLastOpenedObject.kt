package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

/**
 * Use case for clearing last open object's id from user session.
 * @see SaveLastOpenedObject
 */
class ClearLastOpenedObject @Inject constructor(
    private val repo: UserSettingsRepository
) : BaseUseCase<Unit, ClearLastOpenedObject.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.clearLastOpenedObject(params.space)
    }

    data class Params(val space: SpaceId)
}