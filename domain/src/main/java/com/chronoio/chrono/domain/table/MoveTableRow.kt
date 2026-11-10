package com.chronoio.chrono.domain.table

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Position
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class MoveTableRow(
    private val repo: BlockRepository
) : BaseUseCase<Payload, MoveTableRow.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.move(
            command = Command.Move(
                ctx = params.context,
                targetContextId = params.rowContext,
                blockIds = listOf(params.row),
                targetId = params.targetDrop,
                position = params.position
            )
        )
    }

    /**
     * Params for moving a row
     * @param context context for this action (i.e. a page's id or a dashboard's id)
     * @param rowContext context for target (used primarily for cross-page drag-and-drop)
     * @param row id of the row which we want to move
     * @param targetDrop id of the row in relation to which the move is positioned
     * @param position position of the moved [row], relative to [targetDrop]
     */
    data class Params(
        val context: Id,
        val rowContext: Id,
        val row: Id,
        val targetDrop: Id,
        val position: Position
    )
}