package com.chronoio.chrono.domain.download

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class ProcessCancel @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ProcessCancel.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.ProcessCancel(
            processId = params.processId
        )
        return repository.processCancel(command)
    }

    data class Params(
        val processId: String
    )
}