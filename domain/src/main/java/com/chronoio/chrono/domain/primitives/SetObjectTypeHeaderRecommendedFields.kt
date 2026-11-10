package com.chronoio.chrono.domain.primitives

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.primitives.SetObjectTypeHeaderRecommendedFields.Params
import javax.inject.Inject

class SetObjectTypeHeaderRecommendedFields @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.ObjectTypeSetRecommendedHeaderFields(
            objectTypeId = params.objectTypeId,
            fields = params.fields
        )
        return repo.objectTypeSetRecommendedHeaderFields(command = command)
    }

    data class Params(
        val objectTypeId: String,
        val fields: List<Id>
    )
}