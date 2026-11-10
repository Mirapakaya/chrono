package com.chronoio.chrono.domain.spaces

import com.chronoio.chrono.core_models.Struct
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetSpaceDetails @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<SetSpaceDetails.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.setSpaceDetails(
            space = params.space,
            details = params.details
        )
    }

    class Params(val space: SpaceId, val details: Struct)
}