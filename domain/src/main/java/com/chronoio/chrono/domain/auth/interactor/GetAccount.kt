package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.core_models.Account
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

class GetAccount @Inject constructor(
    private val repo: AuthRepository,
    dispatcher: AppCoroutineDispatchers
) : ResultInteractor<Unit, Account>(dispatcher.io) {

    override suspend fun doWork(params: Unit): Account {
        return repo.getCurrentAccount()
    }
}