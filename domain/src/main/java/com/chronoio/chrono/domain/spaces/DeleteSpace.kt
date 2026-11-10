package com.chronoio.chrono.domain.spaces

import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class DeleteSpace @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SpaceId, Unit>(dispatchers.io)  {

    override suspend fun doWork(params: SpaceId) {
        repo.deleteSpace(space = params)
    }
}