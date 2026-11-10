package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Position
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.interactor.CreateBlock.Params
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for creating a block.
 * @see Params
 */
open class CreateBlock @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Pair<Id, Payload>>(dispatchers.io) {

    override suspend fun doWork(params: Params): Pair<Id, Payload> =
        repo.create(
            command = Command.Create(
                context = params.context,
                target = params.target,
                prototype = params.prototype,
                position = params.position
            )
        )

    /**
     * Params for creating a block
     * @property context id of the context of the block (i.e. page, dashboard or something else)
     * @property target id of the block associated with the block we need to create
     * @property position position of the block that we need to create in relation with the target block
     * @property prototype a prototype of the block we would like to create
     */
    data class Params(
        val context: Id,
        val target: Id,
        val position: Position,
        val prototype: Block.Prototype
    )
}