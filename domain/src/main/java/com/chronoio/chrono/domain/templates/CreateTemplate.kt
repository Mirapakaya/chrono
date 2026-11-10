package com.chronoio.chrono.domain.templates

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.CreateObjectResult
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectTypeUniqueKeys
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.primitives.TypeKey
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreateTemplate @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateTemplate.Params, CreateObjectResult>(dispatchers.io) {

    override suspend fun doWork(params: Params): CreateObjectResult {
        val command = Command.CreateObject(
            template = null,
            prefilled = mapOf(Relations.TARGET_OBJECT_TYPE to params.targetObjectTypeId),
            internalFlags = emptyList(),
            space = params.spaceId,
            typeKey = TypeKey(ObjectTypeUniqueKeys.TEMPLATE)
        )
        val result = repo.createObject(command)
        return result
    }

    data class Params(
        val targetObjectTypeId: Id,
        val spaceId: SpaceId
    )
}