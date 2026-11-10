package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.InternalFlags
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetObjectInternalFlags(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetObjectInternalFlags.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        val command = Command.SetInternalFlags(
            ctx = params.ctx,
            flags = params.flags
        )
        return repo.setInternalFlags(command)
    }

    data class Params(
        val ctx: Id,
        val flags: List<InternalFlags>
    )
}