package com.chronoio.chrono.domain.dataview.interactor

import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.DVViewer
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.dataview.interactor.DuplicateDataViewViewer.Params
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor

/**
 * Use-case for duplicating data view's view.
 * @see [Params] for details.
 */
class DuplicateDataViewViewer(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Pair<Id, Payload>>(dispatchers.io) {

    override suspend fun doWork(params: Params): Pair<Id, Payload> {
        return repo.duplicateDataViewViewer(
            context = params.context,
            target = params.target,
            viewer = params.viewer
        )
    }

    /**
     * @property [context] operation context (can be object-set's id or something else)
     * @property [target] id of the data-view block
     * @property [viewer] specific viewer of this [target]
     */
    data class Params(
        val context: Id,
        val target: Id,
        val viewer: DVViewer
    )
}