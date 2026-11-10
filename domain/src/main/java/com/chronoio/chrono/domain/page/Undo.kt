package com.chronoio.chrono.domain.page

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

/**
 * Use-case for un-doing latest changes in document.
 */
class Undo @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Undo.Params, Undo.Result>(dispatchers.io) {

    override suspend fun doWork(params: Undo.Params): Undo.Result {
        return repo.undo(
            command = Command.Undo(
                context = params.context
            )
        )
    }

    /**
     * Params for un-doing latest changes in document.
     * @property context id of the context
     */
    data class Params(
        val context: Id
    )

    sealed class Result {
        data class Success(val payload: Payload) : Result()
        object Exhausted : Result()
    }
}