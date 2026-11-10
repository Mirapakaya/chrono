package com.chronoio.chrono.domain.page.bookmark

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload

/**
 * Use-case for setting up (i.e. fetching) a bookmark from url.
 */
class SetupBookmark(
    private val repo: BlockRepository
) : BaseUseCase<Payload, SetupBookmark.Params>() {

    override suspend fun run(params: Params) = try {
        repo.setupBookmark(
            command = Command.SetupBookmark(
                context = params.context,
                target = params.target,
                url = params.url
            )
        ).let {
            Either.Right(it)
        }
    } catch (t: Throwable) {
        Either.Left(t)
    }

    /**
     * Params for setting up a bookmark from [url]
     * @property context id of the context
     * @property target id of the target block (future bookmark block)
     * @property url bookmark url
     */
    data class Params(
        val context: Id,
        val target: Id,
        val url: String
    )
}