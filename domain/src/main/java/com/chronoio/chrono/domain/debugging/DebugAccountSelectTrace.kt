package com.chronoio.chrono.domain.debugging

import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class DebugAccountSelectTrace @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DebugAccountSelectTrace.Params, String>(dispatchers.io) {

    override suspend fun doWork(params: Params): String {
        return repo.debugAccountSelectTrace(params.dir)
    }

    data class Params(val dir: String)
}