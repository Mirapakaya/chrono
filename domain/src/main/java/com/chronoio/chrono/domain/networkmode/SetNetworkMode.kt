package com.chronoio.chrono.domain.networkmode

import com.chronoio.chrono.core_models.NetworkModeConfig
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

class SetNetworkMode @Inject constructor(
    private val repository: AuthRepository,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<SetNetworkMode.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        repository.setNetworkMode(params.networkModeConfig)
    }

    data class Params(val networkModeConfig: NetworkModeConfig)
}