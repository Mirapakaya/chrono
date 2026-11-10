package com.chronoio.chrono.domain.history

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.history.DiffVersionResponse
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class DiffVersions @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<DiffVersions.Params, DiffVersionResponse>(dispatchers.io) {

    override suspend fun doWork(params: Params): DiffVersionResponse {
        val command = Command.VersionHistory.DiffVersions(
            objectId = params.objectId,
            spaceId = params.spaceId,
            currentVersion = params.currentVersion,
            previousVersion = params.previousVersion
        )
        return repo.diffVersions(command)
    }

    data class Params(
        val objectId: Id,
        val spaceId: Id,
        val currentVersion: Id,
        val previousVersion: Id
    )
}