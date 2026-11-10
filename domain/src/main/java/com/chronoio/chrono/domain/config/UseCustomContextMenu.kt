package com.chronoio.chrono.domain.config

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either

class UseCustomContextMenu(private val repository: InfrastructureRepository) :
    BaseUseCase<Unit, UseCustomContextMenu.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Unit> = safe {
        if (params.use) {
            repository.enableAnytypeContextMenu()
        } else {
            repository.disableAnytypeContextMenu()
        }
    }

    data class Params(val use: Boolean)
}