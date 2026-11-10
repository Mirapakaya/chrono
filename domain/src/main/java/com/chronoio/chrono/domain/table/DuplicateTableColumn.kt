package com.chronoio.chrono.domain.table

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Position
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class DuplicateTableColumn(
    private val repo: BlockRepository
) : BaseUseCase<Payload, DuplicateTableColumn.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.duplicateTableColumn(
            ctx = params.ctx,
            targetId = params.targetDrop,
            blockId = params.column,
            position = params.position
        )
    }

    /**
     * @param ctx - id of the context object
     * @param column - column to duplicate
     * @param targetDrop - id of the column in relation to which the duplicate is positioned
     * @param position - position of the new column, relative to [targetDrop]
     */
    data class Params(
        val ctx: Id,
        val column: Id,
        val targetDrop: Id,
        val position: Position
    )
}