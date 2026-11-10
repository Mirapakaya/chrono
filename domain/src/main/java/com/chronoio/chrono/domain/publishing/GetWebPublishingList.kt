package com.chronoio.chrono.domain.publishing

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.publishing.Publishing
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetWebPublishingList @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetWebPublishingList.Params, List<Publishing.State>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<Publishing.State> {
        val command = Command.Publishing.GetList(
            space = params.space
        )
        return repository.publishingGetList(command)
    }

    data class Params(
        val space: SpaceId?
    )
}