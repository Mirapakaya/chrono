package com.chronoio.chrono.domain.workspace

import com.chronoio.chrono.core_models.NodeUsageInfo
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SpacesUsageInfo(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, NodeUsageInfo>(dispatchers.io) {

    override suspend fun doWork(params: Unit): NodeUsageInfo {
        return repo.nodeUsage()
    }
}