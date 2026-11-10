package com.chronoio.chrono.domain.primitives

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Command.ObjectTypeConflictingFields
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.primitives.SetObjectTypeRecommendedFields.Params
import javax.inject.Inject

class SetObjectTypeRecommendedFields @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.ObjectTypeSetRecommendedFields(
            objectTypeId = params.objectTypeId,
            fields = params.fields
        )
        return repo.objectTypeSetRecommendedFields(command = command)
    }

    data class Params(
        val objectTypeId: String,
        val fields: List<Id>
    )
}