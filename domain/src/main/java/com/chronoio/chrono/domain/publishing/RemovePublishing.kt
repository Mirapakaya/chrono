package com.chronoio.chrono.domain.publishing

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class RemovePublishing @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<RemovePublishing.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.Publishing.Remove(
            space = params.space,
            objectId = params.objectId
        )
        repository.publishingRemove(command)
    }

    data class Params(
        val space: SpaceId,
        val objectId: Id
    )
}
