package com.chronoio.chrono.domain.debugging

import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.workspace.SpaceManager
import javax.inject.Inject

class DebugSpace @Inject constructor(
    private val repo: BlockRepository,
    private val spaceManager: SpaceManager,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, String>(dispatchers.io) {

    override suspend fun doWork(params: Unit): String = repo.debugSpace(
        space = spaceManager.get().let { id ->
            if (id.isEmpty())
                throw IllegalStateException("Space not found")
            else
                SpaceId(id)
        }
    )
}