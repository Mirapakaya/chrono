package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SendJoinSpaceRequest @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<SendJoinSpaceRequest.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.sendJoinSpaceRequest(
            Command.SendJoinSpaceRequest(
                space = params.space,
                network = params.network,
                inviteContentId = params.inviteContentId,
                inviteFileKey = params.inviteFileKey
            )
        )
    }

    /**
     * @property [network] provide network id in case of the self-hosted configuration.
     */
    data class Params(
        val space: SpaceId,
        val network: Id?,
        val inviteContentId: Id,
        val inviteFileKey: Id
    )
}