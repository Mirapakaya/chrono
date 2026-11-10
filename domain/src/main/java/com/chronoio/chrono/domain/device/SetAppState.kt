package com.chronoio.chrono.domain.device

import com.chronoio.chrono.core_models.AppState
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetAppState @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<SetAppState.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repository.setAppState(params.state)
    }

    data class Params(val state: AppState)
} 