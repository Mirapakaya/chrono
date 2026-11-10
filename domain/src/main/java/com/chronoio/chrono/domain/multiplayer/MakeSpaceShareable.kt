package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for making private space shareable.
 * If space is not shareable, you cannot generate an invite link for it.
 */
class MakeSpaceShareable @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SpaceId, Unit>(dispatchers.io) {

    override suspend fun doWork(params: SpaceId) {
        repo.makeSpaceShareable(space = params)
    }
}