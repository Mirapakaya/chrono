package com.chronoio.chrono.domain.dataview.interactor

import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.DVViewerType
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor

/**
 * Use-case for adding a new viewer to DV.
 */
class AddDataViewViewer(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<AddDataViewViewer.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        return repo.addDataViewViewer(
            ctx = params.ctx,
            target = params.target,
            name = params.name,
            type = params.type
        )
    }

    /**
     * @property [context] operation context (a.k.a object set id)
     * @property [target] DV block id
     * @property [name] viewer name
     * @property [type] viewer type
     */
    class Params(
        val ctx: Id,
        val target: Id,
        val name: String,
        val type: DVViewerType
    )
}