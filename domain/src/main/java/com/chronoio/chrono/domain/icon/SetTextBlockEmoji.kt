package com.chronoio.chrono.domain.icon

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetTextBlockEmoji(
    private val repo: BlockRepository
) : SetEmojiIcon<TextBlockTarget>() {

    override suspend fun run(
        params: Params<TextBlockTarget>
    ) = safe {
        repo.setTextIcon(
            command = Command.SetTextIcon(
                context = params.target.context,
                blockId = params.target.blockId,
                icon = Command.SetTextIcon.Icon.Emoji(params.emoji)
            )
        )
    }
}