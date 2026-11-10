package com.chronoio.chrono.domain.collections

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class AddObjectToCollection @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<AddObjectToCollection.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        val command = Command.AddObjectToCollection(
            ctx = params.ctx,
            afterId = params.after,
            ids = params.targets
        )
        return repo.addObjectToCollection(command)
    }

    class Params(
        val ctx: Id,
        val targets: List<Id>,
        val after: Id = ""
    )
}