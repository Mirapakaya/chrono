package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.core_models.Command.Move
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Position
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.interactor.MoveOld.Params
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for moving a group of blocks (cross-document, inside one document, one block after another, etc).
 * @see Params for details.
 */
@Deprecated(
    "Use Move instead",
    replaceWith = ReplaceWith(
        "Move"
    )
)
class MoveOld(private val repo: BlockRepository) : BaseUseCase<Payload, Params>() {

    override suspend fun run(params: Params) = safe {
        repo.move(
            command = Move(
                ctx = params.context,
                targetId = params.targetId,
                targetContextId = params.targetContext,
                position = params.position,
                blockIds = params.blockIds
            )
        )
    }

    /**
     * Params for moving a group of blocks.
     * @param context context for this action (i.e. a page's id or a dashboard's id)
     * @param targetId id of the target block (i.e. target of a drag-and-drop action). Can be empty.
     * @param blockIds id of the blocks that are being dragged and dropped (as opposed to the target block).
     * @param targetContext context for target (used primarily for cross-page drag-and-drop)
     * @param position position of the blocks that are being dragged and dropped related to the target block
     * @see Position for details
     */
    data class Params(
        val context: Id,
        val targetId: Id,
        val targetContext: Id,
        val blockIds: List<Id>,
        val position: Position
    )
}