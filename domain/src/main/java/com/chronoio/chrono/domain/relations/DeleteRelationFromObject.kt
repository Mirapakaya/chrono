package com.chronoio.chrono.domain.relations

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Key
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for deleting a relation from an object.
 */
class DeleteRelationFromObject(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DeleteRelationFromObject.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        return repo.deleteRelationFromObject(
            ctx = params.ctx,
            relations = params.relations
        )
    }

    /**
     * @param ctx id of the object
     * @param relations relation keys
     */
    class Params(
        val ctx: Id,
        val relations: List<Key>
    )
}