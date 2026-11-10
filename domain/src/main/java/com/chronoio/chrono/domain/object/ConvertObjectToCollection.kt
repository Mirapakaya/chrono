package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class ConvertObjectToCollection(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ConvertObjectToCollection.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) = repo.objectToCollection(ctx = params.ctx)

    data class Params(val ctx: Id)
}