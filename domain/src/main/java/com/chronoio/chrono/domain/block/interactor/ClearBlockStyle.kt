package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class ClearBlockStyle(
    private val repository: BlockRepository,
) : BaseUseCase<Payload, ClearBlockStyle.Params>() {

    data class Params(
        val ctx: Id,
        val blockIds: List<Id>
    )

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repository.clearBlockStyle(
            ctx = params.ctx,
            blockIds = params.blockIds
        )
    }
}