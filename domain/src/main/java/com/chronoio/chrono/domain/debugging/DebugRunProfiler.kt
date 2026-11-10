package com.chronoio.chrono.domain.debugging

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

class DebugRunProfiler @Inject constructor(
    private val repo: AuthRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DebugRunProfiler.Params, String>(dispatchers.io) {

    override suspend fun doWork(params: Params): String {
        return repo.debugRunProfiler(params.durationInSeconds)
    }

    data class Params(val durationInSeconds: Int)
}
