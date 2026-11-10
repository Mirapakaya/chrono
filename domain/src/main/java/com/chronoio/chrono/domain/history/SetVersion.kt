package com.chronoio.chrono.domain.history

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetVersion @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<SetVersion.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.VersionHistory.SetVersion(
            objectId = params.objectId,
            versionId = params.versionId
        )
        repo.setVersion(command)
    }

    data class Params(
        val objectId: Id,
        val versionId: Id
    )
}