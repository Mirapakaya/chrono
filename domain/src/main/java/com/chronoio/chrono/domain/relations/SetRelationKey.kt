package com.chronoio.chrono.domain.relations

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetRelationKey(
    private val repo: BlockRepository
) : BaseUseCase<Payload, SetRelationKey.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.setRelationKey(
            Command.SetRelationKey(
                contextId = params.contextId,
                blockId = params.blockId,
                key = params.key
            )
        )
    }

    class Params(
        val contextId: Id,
        val blockId: Id,
        val key: Id
    )
}