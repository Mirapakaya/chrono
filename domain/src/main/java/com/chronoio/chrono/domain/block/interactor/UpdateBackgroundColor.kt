package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload

/**
 * Use-case for updating the whole block's text color.
 */
open class UpdateBackgroundColor(
    private val repo: BlockRepository
) : BaseUseCase<Payload, UpdateBackgroundColor.Params>() {

    override suspend fun run(params: Params) = try {
        repo.updateBackgroundColor(
            command = Command.UpdateBackgroundColor(
                context = params.context,
                targets = params.targets,
                color = params.color
            )
        ).let {
            Either.Right(it)
        }
    } catch (t: Throwable) {
        Either.Left(t)
    }

    /**
     * Params for updating background color for the whole block.
     * @property context context id
     * @property targets id of the target block, whose background color we need to update.
     * @property color new color (hex)
     */
    data class Params(
        val context: Id,
        val targets: List<Id>,
        val color: String
    )
}