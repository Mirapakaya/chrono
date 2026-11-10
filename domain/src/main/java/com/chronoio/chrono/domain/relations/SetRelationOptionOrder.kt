package com.chronoio.chrono.domain.relations

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Key
import com.chronoio.chrono.core_models.primitives.RelationKey
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetRelationOptionOrder @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetRelationOptionOrder.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.SetRelationOptionsOrder(
            space = params.spaceId,
            relationKey = params.relationKey,
            orderedIds = params.orderedIds
        )
        repo.setRelationOptionOrder(
            command
        )
    }

    data class Params(
        val spaceId: SpaceId,
        val relationKey: RelationKey,
        val orderedIds: List<Id>
    )
}
