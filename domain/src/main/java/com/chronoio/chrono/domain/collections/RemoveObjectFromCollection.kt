package com.chronoio.chrono.domain.collections

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use case to remove one or more objects from a collection.
 */
class RemoveObjectFromCollection @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<RemoveObjectFromCollection.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        val command = Command.RemoveObjectFromCollection(
            ctx = params.collectionId,
            ids = params.objectIdsToRemove
        )
        return repo.removeObjectFromCollection(command)
    }

    /**
     * @param collectionId The ID of the collection from which objects will be removed.
     * @param objectIdsToRemove The list of object IDs to remove from the collection.
     */
    data class Params(
        val collectionId: Id,
        val objectIdsToRemove: List<Id>
    )
}