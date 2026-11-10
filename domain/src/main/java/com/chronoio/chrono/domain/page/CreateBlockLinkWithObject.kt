package com.chronoio.chrono.domain.page

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.InternalFlags
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Position
import com.chronoio.chrono.core_models.Struct
import com.chronoio.chrono.core_models.primitives.TypeId
import com.chronoio.chrono.core_models.primitives.TypeKey
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * UseCase for creating a new Object with block linked to this Object
 */

class CreateBlockLinkWithObject(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateBlockLinkWithObject.Params, CreateBlockLinkWithObject.Result>(dispatchers.io) {

    override suspend fun doWork(params: Params): Result {

        val command = Command.CreateBlockLinkWithObject(
            context = params.context,
            target = params.target,
            position = params.position,
            prefilled = emptyMap(),
            template = params.template,
            internalFlags = listOf(InternalFlags.ShouldSelectTemplate),
            type = params.typeKey,
            space = params.space
        )

        val result = repo.createBlockLinkWithObject(command)

        return Result(
            id = result.blockId,
            objectId = result.objectId,
            payload = result.event,
            details = result.details
        )
    }

    /**
     * Params for creating a new object
     * @property context id of the context of the block (i.e. page, dashboard or something else)
     * @property target id of the block associated with the block we need to create
     * @property position position of the block that we need to create in relation with the target block
     * @property type assumed type of new object
     */
    data class Params(
        val context: Id,
        val target: Id,
        val position: Position,
        val typeId: TypeId,
        val typeKey: TypeKey,
        val template: Id?,
        val space: Id
    )

    /**
     * Result for this use-case
     * @property id id of the new block (link)
     * @property objectId id of the new Object linked to this new block[id]
     * @property payload payload of events
     */
    data class Result(
        val id: Id,
        val objectId: Id,
        val details: Struct,
        val payload: Payload
    )
}