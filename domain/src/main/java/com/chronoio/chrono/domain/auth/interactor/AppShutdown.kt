package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

class AppShutdown @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repository: AuthRepository
) : ResultInteractor<Unit, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Unit) {
        repository.appShutdown()
    }
}