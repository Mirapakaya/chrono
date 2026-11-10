package com.chronoio.chrono.domain.icon

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for setting emoji icon.
 */
class SetDocumentEmojiIcon(
    private val repo: BlockRepository
) : SetEmojiIcon<Id>() {

    override suspend fun run(params: SetEmojiIcon.Params<Id>) = safe {
        repo.setDocumentEmojiIcon(
            command = Command.SetDocumentEmojiIcon(
                context = params.target,
                emoji = params.emoji
            )
        )
    }
}