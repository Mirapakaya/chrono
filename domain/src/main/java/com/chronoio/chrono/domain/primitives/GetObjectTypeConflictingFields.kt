package com.chronoio.chrono.domain.primitives

import com.chronoio.chrono.core_models.Command.ObjectTypeConflictingFields
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetObjectTypeConflictingFields @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetObjectTypeConflictingFields.Params, List<Id>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<Id> {
        val command = ObjectTypeConflictingFields(
            spaceId = params.spaceId,
            objectTypeId = params.objectTypeId
        )
        return repo.objectTypeListConflictingRelations(command = command)
    }

    data class Params(
        val spaceId: String,
        val objectTypeId: String
    )
}