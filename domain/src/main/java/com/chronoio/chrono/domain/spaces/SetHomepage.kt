package com.chronoio.chrono.domain.spaces

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetHomepage @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<SetHomepage.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repo.setHomepage(
            Command.SetHomepage(
                spaceId = params.spaceId,
                homepage = params.homepage
            )
        )
    }

    data class Params(val spaceId: Id, val homepage: Id)
}
