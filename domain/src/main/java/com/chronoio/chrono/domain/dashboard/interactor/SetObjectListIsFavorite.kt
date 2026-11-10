package com.chronoio.chrono.domain.dashboard.interactor

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetObjectListIsFavorite @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetObjectListIsFavorite.Params, Unit>(dispatchers.io) {

    data class Params(
        val objectIds: List<Id>,
        val isFavorite: Boolean
    )

    override suspend fun doWork(params: Params) =
        repo.setObjectListIsFavorite(params.objectIds, params.isFavorite)
}