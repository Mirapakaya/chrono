package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload

/**
 * Use-case for merging a pair of blocks.
 */
open class MergeBlocks(private val repo: BlockRepository) :
    BaseUseCase<Payload, MergeBlocks.Params>() {

    override suspend fun run(params: Params) = try {
        repo.merge(
            command = Command.Merge(
                context = params.context,
                pair = params.pair
            )
        ).let {
            Either.Right(it)
        }
    } catch (t: Throwable) {
        Either.Left(t)
    }

    /**
     * Params for merging two blocks into one block
     * @property context context id
     * @property pair pair of the blocks, which we need to merge
     */
    data class Params(
        val context: Id,
        val pair: Pair<Id, Id>
    )
}