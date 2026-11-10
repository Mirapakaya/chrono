package com.chronoio.chrono.domain.history

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.history.Version
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetVersions @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<GetVersions.Params, List<Version>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<Version> {
        val command = Command.VersionHistory.GetVersions(
            objectId = params.objectId,
            lastVersion = params.lastVersion,
            limit = params.limit
        )
        return repo.getVersions(command)
    }

    data class Params(
        val objectId: Id,
        val lastVersion: Id? = null,
        val limit: Int = 300
    )
}