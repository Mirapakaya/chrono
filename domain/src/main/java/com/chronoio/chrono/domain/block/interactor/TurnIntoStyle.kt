package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository

class TurnIntoStyle(
    private val repo: BlockRepository
) : BaseUseCase<Payload, TurnIntoStyle.Params>() {

    override suspend fun run(
        params: Params
    ) = safe {
        repo.turnInto(
            context = params.context,
            targets = params.targets,
            style = params.style
        )
    }

    data class Params(
        val context: Id,
        val targets: List<Id>,
        val style: Block.Content.Text.Style
    )
}