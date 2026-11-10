package com.chronoio.chrono.domain.widgets

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.ResultatInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for updating widget block contained in widgets.
 */
class UpdateWidget @Inject constructor(
    private val repo: BlockRepository
) : ResultatInteractor<UpdateWidget.Params, Payload>() {

    override suspend fun execute(params: Params): Payload = repo.updateWidget(
        ctx = params.ctx,
        source = params.source,
        type = params.type,
        widget = params.widget
    )

    /**
     * [ctx] context of widgets — id of object containing widgets
     * [widget] widget block to update
     * [source] source for the given widget block
     * [type] layout type to update
     */
    data class Params(
        val ctx: Id,
        val widget: Id,
        val source: Id,
        val type: Block.Content.Widget.Layout
    )
}