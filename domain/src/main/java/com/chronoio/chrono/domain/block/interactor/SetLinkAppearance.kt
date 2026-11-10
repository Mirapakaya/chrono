package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetLinkAppearance(
    private val repository: BlockRepository,
) : BaseUseCase<Payload, SetLinkAppearance.Params>() {

    data class Params(
        val contextId: String,
        val blockId: String,
        val content: Block.Content.Link
    )

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repository.setLinkAppearance(
            Command.SetLinkAppearance(
                contextId = params.contextId,
                blockId = params.blockId,
                content = params.content
            )
        )
    }
}