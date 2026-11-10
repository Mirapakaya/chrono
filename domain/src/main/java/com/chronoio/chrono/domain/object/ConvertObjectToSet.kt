package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class ConvertObjectToSet(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ConvertObjectToSet.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) =
        repo.objectToSet(
            ctx = params.ctx,
            source = params.sources
        )

    data class Params(
        val ctx: Id,
        val sources: List<String>
    )
}