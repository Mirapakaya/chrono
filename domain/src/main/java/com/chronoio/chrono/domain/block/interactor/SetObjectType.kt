package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Key
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetObjectType(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetObjectType.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        return repo.setObjectTypeToObject(
            ctx = params.context,
            objectTypeKey = params.objectTypeKey
        )
    }

    class Params(
        val context: Id,
        val objectTypeKey: Key
    )
}