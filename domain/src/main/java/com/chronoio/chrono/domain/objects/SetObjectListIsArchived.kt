package com.chronoio.chrono.domain.objects

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived.Params
import javax.inject.Inject

/**
 * Use-case for archiving (or restoring from archive) a list of objects.
 */
class SetObjectListIsArchived @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Unit>(dispatchers.io) {

    /**
     * Params for archiving a list of objects.
     * @property [targets] id of the objects to archive/restore.
     */
    data class Params(
        val targets: List<Id>,
        val isArchived: Boolean
    )

    override suspend fun doWork(params: Params) = repo.setObjectListIsArchived(
        params.targets, params.isArchived
    )
}