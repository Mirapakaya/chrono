package com.chronoio.chrono.domain.relations

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.RelationListWithValueItem
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.relations.GetObjectRelationListById.Params
import javax.inject.Inject

class GetObjectRelationListById @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, List<RelationListWithValueItem>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<RelationListWithValueItem> {
        val command = Command.RelationListWithValue(
            space = params.space,
            value = params.value
        )
        return repo.objectRelationListWithValue(command)
    }

    data class Params(val space: SpaceId, val value: Id)
}