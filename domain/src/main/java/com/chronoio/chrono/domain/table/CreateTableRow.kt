package com.chronoio.chrono.domain.table

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Position
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class CreateTableRow(
    private val repo: BlockRepository
) : BaseUseCase<Payload, CreateTableRow.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.createTableRow(
            ctx = params.ctx,
            targetId = params.target,
            position = params.position
        )
    }

    /**
     * @param ctx - id of the context object
     * @param target - id of the closest row
     * @param position - position of the new row, relative to [target]
     */
    data class Params(
        val ctx: Id,
        val target: Id,
        val position: Position
    )
}