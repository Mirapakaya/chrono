package com.chronoio.chrono.domain.widgets

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for setting active view id for widget with list or compact list layout.
 */
class SetWidgetActiveView @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetWidgetActiveView.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params) = repo.setWidgetViewId(
        ctx = params.ctx,
        widget = params.widget,
        view = params.view
    )

    data class Params(
        val ctx: Id,
        val widget: Id,
        val view: Id
    )
}