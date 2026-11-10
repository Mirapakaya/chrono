package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.multiplayer.SpaceInviteView
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetSpaceInviteView @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetSpaceInviteView.Params, SpaceInviteView>(dispatchers.io) {

    override suspend fun doWork(params: Params): SpaceInviteView {
        return repo.getSpaceInviteView(
            inviteFileKey = params.inviteFileKey,
            inviteContentId = params.inviteContentId
        )
    }

    data class Params(
        val inviteContentId: String,
        val inviteFileKey: String
    )
}