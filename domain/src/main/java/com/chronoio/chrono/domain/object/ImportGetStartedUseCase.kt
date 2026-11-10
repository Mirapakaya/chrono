package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class ImportGetStartedUseCase(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<ImportGetStartedUseCase.Params, Command.ImportUseCase.Result>(dispatchers.io) {

    override suspend fun doWork(params: Params): Command.ImportUseCase.Result {
        return repo.importGetStartedUseCase(params.space)
    }

    data class Params(val space: Id)
}