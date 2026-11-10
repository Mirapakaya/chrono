package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.multiplayer.SpaceMemberPermissions
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class AddSpaceMembers @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<AddSpaceMembers.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.addSpaceMembers(
            space = params.space,
            identities = params.identities,
            permissions = params.permissions
        )
    }

    /**
     * @property [identities] identities of members to add to a given space
     * @property [permissions] permissions to assign to the added members
     */
    data class Params(
        val space: SpaceId,
        val identities: List<Id>,
        val permissions: SpaceMemberPermissions = SpaceMemberPermissions.READER
    )
}
