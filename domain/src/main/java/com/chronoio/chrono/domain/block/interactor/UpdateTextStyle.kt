package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.core_models.Block.Content.Text
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload

/**
 * Use-case for updating a block's text style
 */
open class UpdateTextStyle(
    private val repo: BlockRepository
) : BaseUseCase<Payload, UpdateTextStyle.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.updateTextStyle(
            command = Command.UpdateStyle(
                style = params.style,
                context = params.context,
                targets = params.targets
            )
        )
    }

    /**
     * @property context context id
     * @property targets id of the target blocks, whose style we need to update.
     * @property style new style for the target block.
     */
    data class Params(
        val context: Id,
        val targets: List<Id>,
        val style: Text.Style
    )
}