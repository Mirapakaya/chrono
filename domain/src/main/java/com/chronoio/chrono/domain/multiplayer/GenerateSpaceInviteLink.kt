package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.multiplayer.InviteType
import com.chronoio.chrono.core_models.multiplayer.SpaceInviteLink
import com.chronoio.chrono.core_models.multiplayer.SpaceMemberPermissions
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GenerateSpaceInviteLink @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<GenerateSpaceInviteLink.Params, SpaceInviteLink>(dispatchers.io) {

    override suspend fun doWork(params: Params): SpaceInviteLink {
        return repo.generateSpaceInviteLink(
            space = params.space,
            inviteType = params.inviteType,
            permissions = params.permissions,
            shareWithinSpace = params.shareWithinSpace
        )
    }

    data class Params(
        val space: SpaceId,
        val inviteType: InviteType?,
        val permissions: SpaceMemberPermissions?,
        val shareWithinSpace: Boolean = false
    )
}