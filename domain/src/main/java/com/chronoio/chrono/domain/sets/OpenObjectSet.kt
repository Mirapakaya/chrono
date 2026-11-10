package com.chronoio.chrono.domain.sets

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Result
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository

@Deprecated("Legacy. Need to migrate to OpenObject useCase")
class OpenObjectSet(
    private val repo: BlockRepository,
    private val settings: UserSettingsRepository
) : BaseUseCase<Result<Payload>, OpenObjectSet.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.openObjectSet(
            id = params.obj,
            space = params.space
        ).also {
            settings.setLastOpenedObject(
                id = params.obj,
                space = params.space
            )
        }
    }

    data class Params(
        val space: SpaceId,
        val obj: Id
    )
}