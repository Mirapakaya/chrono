package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class RemoveSpaceMembers @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<RemoveSpaceMembers.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.removeSpaceMembers(
            space = params.space,
            identities = params.identities
        )
    }

    /**
     * @property [identities] identities of members of a given space
     */
    data class Params(val space: SpaceId, val identities: List<Id>)
}