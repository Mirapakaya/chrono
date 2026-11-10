package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class DeclineSpaceJoinRequest @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DeclineSpaceJoinRequest.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.declineSpaceRequest(
            space = params.space,
            identity = params.identity
        )
    }

    /**
     * @property [identity] members identity
     * @see Relations.IDENTITY
     */
    data class Params(
        val space: SpaceId,
        val identity: Id
    )
}