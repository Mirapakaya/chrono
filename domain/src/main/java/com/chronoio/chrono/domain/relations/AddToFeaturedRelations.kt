package com.chronoio.chrono.domain.relations

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for adding one or more relations to featured relations list.
 */
class AddToFeaturedRelations(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<AddToFeaturedRelations.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        return repo.addToFeaturedRelations(
            ctx = params.ctx,
            relations = params.relations
        )
    }

    /**
     * @property [relations] list of ids to add to existing ones.
     */
    class Params(
        val ctx: Id,
        val relations: List<Id>
    )
}