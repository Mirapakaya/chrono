package com.chronoio.chrono.domain.objects

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for deleting objects.
 * @see SetObjectListIsArchived
 */
class DeleteObjects @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DeleteObjects.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) = repo.deleteObjects(params.targets)

    /**
     * @property [targets] id of the objects to delete.
     */
    class Params(val targets: List<Id>)
}