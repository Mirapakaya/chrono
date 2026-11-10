package com.chronoio.chrono.domain.block

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Payload

/**
 * Use-case for updating a divider style
 */
open class UpdateDivider(
    private val repo: BlockRepository
) : BaseUseCase<Payload, UpdateDivider.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.updateDivider(
            command = Command.UpdateDivider(
                style = params.style,
                context = params.context,
                targets = params.targets
            )
        )
    }

    /**
     * @property context context id
     * @property targets id of the target blocks, whose style we need to update.
     * @property style new style for the target blocks.
     */
    data class Params(
        val context: Id,
        val targets: List<Id>,
        val style: Block.Content.Divider.Style
    )
}