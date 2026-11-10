package com.chronoio.chrono.domain.page

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Id

/**
 * Use-case for updating document's title
 */
class UpdateTitle(
    private val repo: BlockRepository
) : BaseUseCase<Unit, UpdateTitle.Params>() {

    override suspend fun run(params: Params) = try {
        repo.updateDocumentTitle(
            command = Command.UpdateTitle(
                context = params.context,
                title = params.title
            )
        ).let {
            Either.Right(it)
        }
    } catch (t: Throwable) {
        Either.Left(t)
    }

    /**
     * Params for updating document's title
     * @property context id of the context
     * @property title new title for the document
     */
    data class Params(
        val context: Id,
        val title: String
    )
}