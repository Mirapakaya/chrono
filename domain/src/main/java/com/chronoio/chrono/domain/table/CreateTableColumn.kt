package com.chronoio.chrono.domain.table

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Position
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class CreateTableColumn(
    private val repo: BlockRepository
) : BaseUseCase<Payload, CreateTableColumn.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.createTableColumn(
            ctx = params.ctx,
            targetId = params.target,
            position = params.position
        )
    }

    /**
     * @param ctx - id of the context object
     * @param target - id of the closest column
     * @param position - position of the new column, relative to [target]
     */
    data class Params(
        val ctx: Id,
        val target: Id,
        val position: Position
    )
}