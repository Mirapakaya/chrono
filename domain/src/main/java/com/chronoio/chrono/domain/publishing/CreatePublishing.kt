package com.chronoio.chrono.domain.publishing

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreatePublishing @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreatePublishing.Params, String>(dispatchers.io) {

    override suspend fun doWork(params: Params): String {
        val command = Command.Publishing.Create(
            space = params.space,
            objectId = params.objectId,
            uri = params.uri,
            showJoinSpaceBanner = params.showJoinSpaceBanner
        )
        return repository.publishingCreate(command)
    }

    data class Params(
        val space: SpaceId,
        val objectId: Id,
        val uri: String,
        val showJoinSpaceBanner: Boolean = false
    )
}
