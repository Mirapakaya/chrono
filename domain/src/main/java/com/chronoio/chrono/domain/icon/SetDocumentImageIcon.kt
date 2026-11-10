package com.chronoio.chrono.domain.icon

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetDocumentImageIcon @Inject constructor(
    private val repo: BlockRepository
) : SetImageIcon<Id>() {

    override suspend fun run(params: Params<Id>) = safe {
        val file = repo.uploadFile(
            command = Command.UploadFile(
                path = params.path,
                type = Block.Content.File.Type.IMAGE,
                space = params.spaceId,
                createdInContext = params.target,
                createdInContextRef = Relations.ICON_IMAGE
            )
        )
        val payload = repo.setDocumentImageIcon(
            command = Command.SetDocumentImageIcon(
                id = file.id,
                context = params.target
            )
        )
        Pair(payload, file.id)
    }
}