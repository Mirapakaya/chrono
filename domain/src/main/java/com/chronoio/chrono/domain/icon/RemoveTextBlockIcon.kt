package com.chronoio.chrono.domain.icon

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Remove text block icon i.e. callout block
 */
class RemoveTextBlockIcon(
    private val repo: BlockRepository
) : RemoveIcon<TextBlockTarget>() {

    override suspend fun run(params: TextBlockTarget) = safe {
        repo.setTextIcon(
            Command.SetTextIcon(
                context = params.context,
                blockId = params.blockId,
                icon = Command.SetTextIcon.Icon.None
            )
        )
    }
}