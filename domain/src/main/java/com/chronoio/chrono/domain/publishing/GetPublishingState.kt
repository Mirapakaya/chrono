package com.chronoio.chrono.domain.publishing

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.publishing.Publishing
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetPublishingState @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetPublishingState.Params, Publishing.State?>(dispatchers.io) {

    override suspend fun doWork(params: Params): Publishing.State? {
        val command = Command.Publishing.GetStatus(
            space = params.space,
            objectId = params.objectId
        )
        return repository.publishingGetStatus(command)
    }

    data class Params(
        val space: SpaceId,
        val objectId: Id
    )
}
