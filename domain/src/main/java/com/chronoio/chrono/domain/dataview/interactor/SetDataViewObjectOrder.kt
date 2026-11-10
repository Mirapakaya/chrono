package com.chronoio.chrono.domain.dataview.interactor

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectOrder
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for changing the order of objects within data view groups
 * (e.g. reordering cards inside a Kanban column).
 */
class SetDataViewObjectOrder(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetDataViewObjectOrder.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload = repo.setDataViewObjectOrder(
        ctx = params.ctx,
        dv = params.dv,
        objectOrders = params.objectOrders
    )

    /**
     * @property [ctx] set or collection id
     * @property [dv] data view block id
     * @property [objectOrders] per-group ordered object ids to persist
     */
    data class Params(
        val ctx: Id,
        val dv: Id,
        val objectOrders: List<ObjectOrder>
    )
}
