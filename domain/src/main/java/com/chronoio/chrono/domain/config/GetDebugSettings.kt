package com.chronoio.chrono.domain.config

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either

class GetDebugSettings(private val repo: InfrastructureRepository) :
    BaseUseCase<DebugSettings, Unit>() {

    override suspend fun run(params: Unit): Either<Throwable, DebugSettings> = safe {
        DebugSettings(isAnytypeContextMenuEnabled = false)
    }
}