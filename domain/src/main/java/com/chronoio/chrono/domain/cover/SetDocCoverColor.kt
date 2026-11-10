package com.chronoio.chrono.domain.cover

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetDocCoverColor(private val repo: BlockRepository) : BaseUseCase<Payload, SetDocCoverColor.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.setDocumentCoverColor(
            ctx = params.ctx,
            color = params.color
        )
    }

    class Params(
        val ctx: Id,
        val color: String
    )
}