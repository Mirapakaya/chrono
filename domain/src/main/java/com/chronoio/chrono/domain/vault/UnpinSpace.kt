package com.chronoio.chrono.domain.vault

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class UnpinSpace @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<UnpinSpace.Params, Unit>(dispatchers.io) {

    data class Params(
        val spaceId: Id
    )

    override suspend fun doWork(params: Params): Unit {
        repo.spaceUnsetOrder(
            spaceViewId = params.spaceId
        )
    }
} 