package com.chronoio.chrono.domain.table

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Position
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class MoveTableColumn(
    private val repo: BlockRepository
) : BaseUseCase<Payload, MoveTableColumn.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.moveTableColumn(
            ctx = params.ctx,
            target = params.column,
            dropTarget = params.targetDrop,
            position = params.position
        )
    }

    /**
     * Params for moving a column
     * @param ctx - id of the context object
     * @param column - id of the column which we want to move
     * @param targetDrop - id of the column in relation to which the move is positioned
     * @param position - position of the moved [column], relative to [targetDrop]
     */
    data class Params(
        val ctx: Id,
        val column: Id,
        val targetDrop: Id,
        val position: Position
    )
}