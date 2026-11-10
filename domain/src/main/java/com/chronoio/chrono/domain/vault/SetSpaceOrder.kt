package com.chronoio.chrono.domain.vault

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetSpaceOrder @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<SetSpaceOrder.Params, List<Id>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<Id> {
        return repository.spaceSetOrder(
            spaceViewId = params.spaceViewId,
            spaceViewOrder = params.spaceViewOrder
        )
    }

    data class Params(
        val spaceViewId: Id,
        val spaceViewOrder: List<Id>
    )
}