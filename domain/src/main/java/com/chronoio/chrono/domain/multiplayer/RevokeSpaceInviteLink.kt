package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.multiplayer.SpaceInviteLinkAccessLevel
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.invite.SpaceInviteLinkStore
import javax.inject.Inject

class RevokeSpaceInviteLink @Inject constructor(
    private val repo: BlockRepository,
    private val store: SpaceInviteLinkStore,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SpaceId, Unit>(dispatchers.io) {
    override suspend fun doWork(params: SpaceId) {
        val result = repo.revokeSpaceInviteLink(space = params)

        // Update the store to reflect that the invite link is now disabled
        store.update(params, SpaceInviteLinkAccessLevel.LinkDisabled())

        return result
    }
}