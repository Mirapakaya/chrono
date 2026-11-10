package com.chronoio.chrono.domain.dataview.interactor

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for ordering views of the data view.
 */
class SetDataViewViewerPosition(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetDataViewViewerPosition.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload = repo.setDataViewViewerPosition(
        ctx = params.ctx,
        view = params.viewer,
        dv = params.dv,
        pos = params.pos
    )

    /**
     * @property [ctx] set or collection id
     * @property [dv] data view block id
     * @property [viewer] view id, whose position is changed
     * @property [pos] new position of the [viewer]
     */
    data class Params(
        val ctx: Id,
        val dv: Id,
        val viewer: Id,
        val pos: Int
    )
}