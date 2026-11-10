package com.chronoio.chrono.domain.icon

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetTextBlockImage(
    private val repo: BlockRepository
) : SetImageIcon<TextBlockTarget>() {

    override suspend fun run(
        params: Params<TextBlockTarget>
    ) = safe {
        val file = repo.uploadFile(
            command = Command.UploadFile(
                path = params.path,
                type = Block.Content.File.Type.IMAGE,
                space = params.spaceId,
                createdInContext = params.target.context,
                createdInContextRef = Relations.ICON_IMAGE
            )
        )
        val payload = repo.setTextIcon(
            command = Command.SetTextIcon(
                icon = Command.SetTextIcon.Icon.Image(file.id),
                context = params.target.context,
                blockId = params.target.blockId
            )
        )
        Pair(payload, file.id)
    }
}