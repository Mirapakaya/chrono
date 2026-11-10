package com.chronoio.chrono.domain.cover

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetDocCoverGradient(private val repo: BlockRepository) : BaseUseCase<Payload, SetDocCoverGradient.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.setDocumentCoverGradient(
            ctx = params.ctx,
            gradient = params.gradient
        )
    }

    class Params(
        val ctx: Id,
        val gradient: String
    )
}