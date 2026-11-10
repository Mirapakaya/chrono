package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Struct
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetObjectDetails @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetObjectDetails.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload = repo.setObjectDetails(
        ctx = params.ctx,
        details = params.details
    )

    data class Params(
        val ctx: Id,
        val details: Struct
    )
}