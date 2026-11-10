package com.chronoio.chrono.domain.workspace

import com.chronoio.chrono.core_models.FileLimitsEvent
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import kotlinx.coroutines.flow.Flow

class InterceptFileLimitEvents(
    private val fileLimitsEventChannel: FileLimitsEventChannel,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, Flow<List<FileLimitsEvent>>>(dispatchers.io) {

    override suspend fun doWork(params: Unit): Flow<List<FileLimitsEvent>> {
        return fileLimitsEventChannel.observe()
    }
}