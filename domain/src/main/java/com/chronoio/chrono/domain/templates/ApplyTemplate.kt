package com.chronoio.chrono.domain.templates

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class ApplyTemplate(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<ApplyTemplate.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.ApplyTemplate(
            objectId = params.ctx,
            template = params.template
        )
        return repo.applyTemplate(command)
    }

    /**
     * @param ctx id of the object to apply template to
     * @param template id of the template to apply, could be null, in that case the blank template will be applied
     */
    data class Params(
        val ctx: Id,
        val template: Id?
    )
}