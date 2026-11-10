package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Payload

class UploadBlock(private val repo: BlockRepository) : BaseUseCase<Payload, UploadBlock.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.uploadBlock(
            command = Command.UploadBlock(
                contextId = params.contextId,
                blockId = params.blockId,
                url = params.url,
                filePath = params.filePath
            )
        )
    }

    data class Params(
        val contextId: String,
        val blockId: String,
        val url: String,
        val filePath: String
    )
}