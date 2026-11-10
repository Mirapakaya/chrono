package com.chronoio.chrono.domain.dataview.interactor

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetActiveViewer(private val repo: BlockRepository, dispatchers: AppCoroutineDispatchers) :
    ResultInteractor<SetActiveViewer.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        val command = Command.DataViewSetActiveView(
            ctx = params.ctx,
            viewerId = params.viewer,
            dataViewId = params.dv
        )
        return repo.dataViewSetActiveView(command)
    }

    data class Params(
        val ctx: Id,
        val viewer: Id,
        val dv: Id
    )
}