package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class ApproveLeaveSpaceRequest @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ApproveLeaveSpaceRequest.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.approveSpaceLeaveRequest(
            command = Command.ApproveSpaceLeaveRequest(
                space = params.space,
                identities = params.identities
            )
        )
    }

    data class Params(
        val space: SpaceId,
        val identities: List<Id>
    )
}