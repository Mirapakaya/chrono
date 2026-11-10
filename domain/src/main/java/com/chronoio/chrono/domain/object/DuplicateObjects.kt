package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class DuplicateObjects(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DuplicateObjects.Params, List<Id>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<Id> {
        return repo.duplicateObjectsList(params.ids)
    }

    data class Params(
        val ids: List<Id>
    )
}