package com.chronoio.chrono.domain.spaces

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.SpaceCreationUseCase
import com.chronoio.chrono.core_models.Struct
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreateSpace @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateSpace.Params, Command.CreateSpace.Result>(dispatchers.io) {

    override suspend fun doWork(params: Params) = repo.createWorkspace(
        command = Command.CreateSpace(
            details = params.details,
            useCase = params.useCase
        )
    )

    data class Params(
        val details: Struct,
        val useCase: SpaceCreationUseCase = SpaceCreationUseCase.GET_STARTED_MOBILE
    )
}