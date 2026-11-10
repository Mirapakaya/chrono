package com.chronoio.chrono.domain.sets

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetQueryToObjectSet(private val repo: BlockRepository, dispatchers: AppCoroutineDispatchers) :
    ResultInteractor<SetQueryToObjectSet.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        val command = Command.SetQueryToSet(
            ctx = params.ctx,
            query = params.query
        )
        return repo.setQueryToSet(command)
    }

    class Params(
        val ctx: Id,
        val query: Id
    )
}