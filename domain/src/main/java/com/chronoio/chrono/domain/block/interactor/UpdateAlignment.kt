package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload

open class UpdateAlignment(
    private val repo: BlockRepository
) : BaseUseCase<Payload, UpdateAlignment.Params>() {

    override suspend fun run(params: Params) = try {
        repo.updateAlignment(
            command = Command.UpdateAlignment(
                context = params.context,
                targets = params.targets,
                alignment = params.alignment
            )
        ).let {
            Either.Right(it)
        }
    } catch (e: Exception) {
        Either.Left(e)
    }

    /**
     * Params for updating alignment of the whole block.
     * @property context context id
     * @property targets id of the target block, whose alignment we need to update.
     * @property alignment new alignment
     */
    data class Params(
        val context: Id,
        val targets: List<Id>,
        val alignment: Block.Align
    )
}