package com.chronoio.chrono.domain.media

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Discards preloaded file by ID.
 */
class DiscardPreloadedFile @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Id, Unit>(dispatchers.io) {
    override suspend fun doWork(params: Id) {
        repo.discardPreloadedFile(
            command = Command.DiscardPreloadedFile(params)
        )
    }
}