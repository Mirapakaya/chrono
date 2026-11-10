package com.chronoio.chrono.domain.layout

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetObjectLayout(private val repo: BlockRepository): BaseUseCase<Payload, SetObjectLayout.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.setObjectLayout(
            ctx = params.ctx,
            layout = params.layout
        )
    }

    data class Params(
        val ctx: Id,
        val layout: ObjectType.Layout
    )
}