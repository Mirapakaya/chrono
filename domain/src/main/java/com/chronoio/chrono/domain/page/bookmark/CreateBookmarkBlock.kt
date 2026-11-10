package com.chronoio.chrono.domain.page.bookmark

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Position
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for creating a bookmark block from url.
 */
class CreateBookmarkBlock(
    private val repo: BlockRepository
) : BaseUseCase<Payload, CreateBookmarkBlock.Params>() {

    override suspend fun run(params: Params) = try {
        repo.createAndFetchBookmarkBlock(
            command = Command.CreateBookmark(
                context = params.context,
                target = params.target,
                url = params.url,
                position = params.position
            )
        ).let {
            Either.Right(it)
        }
    } catch (t: Throwable) {
        Either.Left(t)
    }

    /**
     * Params for creating a bookmark block from [url]
     * @property context id of the context
     * @property target id of the target block (future bookmark block)
     * @property url bookmark url
     * @property [position] positon relative to [target] block
     */
    data class Params(
        val context: Id,
        val target: Id,
        val url: String,
        val position: Position
    )
}