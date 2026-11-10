package com.chronoio.chrono.domain.icon

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.icon.RemoveObjectIcon.Params

class RemoveObjectIcon(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repository.removeDocumentIcon(ctx = params.objectId)
    }

    data class Params(val objectId: Id)
}