package com.chronoio.chrono.domain.notifications

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

class RegisterDeviceToken @Inject constructor(
    private val repository: AuthRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<RegisterDeviceToken.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.RegisterDeviceToken(
            token = params.token,
        )
        repository.registerDeviceToken(command = command)
    }

    data class Params(
        val token: String
    )
}