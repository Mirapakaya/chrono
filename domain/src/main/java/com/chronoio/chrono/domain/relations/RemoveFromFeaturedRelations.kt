package com.chronoio.chrono.domain.relations

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for removing one or more relations from featured relations list.
 */
class RemoveFromFeaturedRelations(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<RemoveFromFeaturedRelations.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        return repo.removeFromFeaturedRelations(
            ctx = params.ctx,
            relations = params.relations
        )
    }

    /**
     * @property [relations] list of ids to remove from existing ones.
     */
    class Params(
        val ctx: Id,
        val relations: List<Id>
    )
}