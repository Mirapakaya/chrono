package com.chronoio.chrono.domain.widgets

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class UpdateObjectTypesOrderIds @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<UpdateObjectTypesOrderIds.Params, List<Id>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<Id> {
        val command = Command.ObjectTypesSetOrder(
            spaceId = params.spaceId.id,
            orderedIds = params.orderedIds
        )
        return repository.objectTypesSetOrder(command)
    }

    data class Params(
        val spaceId: SpaceId,
        val orderedIds: List<String>
    )
}