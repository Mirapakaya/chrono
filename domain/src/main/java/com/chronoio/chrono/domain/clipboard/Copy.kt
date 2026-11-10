package com.chronoio.chrono.domain.clipboard

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Id

class Copy(
    private val repo: BlockRepository,
    private val clipboard: Clipboard
) : BaseUseCase<Unit, Copy.Params>() {

    override suspend fun run(params: Params) = safe {
        val result = repo.copy(
            command = Command.Copy(
                context = params.context,
                range = params.range,
                blocks = params.blocks
            )
        )
        clipboard.put(
            text = result.text,
            html = result.html,
            blocks = result.blocks
        )
    }

    /**
     * Params for clipboard paste operation.
     * @param context id of the context
     * @param range selected text range
     * @param blocks associated blocks
     */
    data class Params(
        val context: Id,
        val range: IntRange?,
        val blocks: List<Block>
    )
}