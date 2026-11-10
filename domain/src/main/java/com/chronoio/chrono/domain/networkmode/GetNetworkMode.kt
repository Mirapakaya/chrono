package com.chronoio.chrono.domain.networkmode

import com.chronoio.chrono.core_models.NetworkModeConfig
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

class GetNetworkMode @Inject constructor(
    private val repository: AuthRepository,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<Unit, NetworkModeConfig>(dispatchers.io) {

    override suspend fun doWork(params: Unit): NetworkModeConfig {
        return repository.getNetworkMode()
    }
}