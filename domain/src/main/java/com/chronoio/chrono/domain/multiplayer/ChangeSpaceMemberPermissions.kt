package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.multiplayer.SpaceMemberPermissions
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class ChangeSpaceMemberPermissions @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ChangeSpaceMemberPermissions.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.changeSpaceMemberPermissions(
            space = params.space,
            identity = params.identity,
            permission = params.permission
        )
    }

    data class Params(
        val space: SpaceId,
        val identity: Id,
        val permission: SpaceMemberPermissions
    )
}