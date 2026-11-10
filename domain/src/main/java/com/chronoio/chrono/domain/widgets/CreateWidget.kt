package com.chronoio.chrono.domain.widgets

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Position
import com.chronoio.chrono.core_models.WidgetLayout
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreateWidget @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateWidget.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        return repo.createWidget(
            ctx = params.ctx,
            source = params.source,
            layout = params.type,
            target = params.target,
            position = params.position
        )
    }

    data class Params(
        val ctx: Id,
        val source: Id,
        val type: WidgetLayout,
        val target: Id? = null,
        val position: Position = Position.NONE
    )
}