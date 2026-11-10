package com.chronoio.chrono.domain.search

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.objects.ObjectStore

class CancelSearchSubscription(
    private val repo: BlockRepository,
    private val store: ObjectStore
) : BaseUseCase<Unit, CancelSearchSubscription.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.cancelObjectSearchSubscription(params.subscriptions)
        store.unsubscribe(params.subscriptions)
    }

    class Params(val subscriptions: List<Id>)
}