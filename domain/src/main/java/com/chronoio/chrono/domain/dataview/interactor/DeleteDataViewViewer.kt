package com.chronoio.chrono.domain.dataview.interactor

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.dataview.interactor.DeleteDataViewViewer.Params


/**
 * Use-case for deleting a view from data view.
 * @see [Params] for details.
 */
class DeleteDataViewViewer(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        return repo.removeDataViewViewer(
            ctx = params.ctx,
            dataview = params.dataview,
            viewer = params.viewer
        )
    }

    /**
     * @property [context] operation context
     * @property [dataview] id of the data view containing a viewer, which we need to delete
     * @property [viewer] id of the viewer, which we need to delete
     */
    data class Params(
        val ctx: Id,
        val dataview: Id,
        val viewer: Id
    )
}