package com.chronoio.chrono.domain.debugging

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

class DebugExportLogs @Inject constructor(
    private val repo: AuthRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DebugExportLogs.Params, String>(dispatchers.io) {

    override suspend fun doWork(params: Params): String {
        return repo.debugExportLogs(params.dir)
    }

    data class Params(val dir: String)
}