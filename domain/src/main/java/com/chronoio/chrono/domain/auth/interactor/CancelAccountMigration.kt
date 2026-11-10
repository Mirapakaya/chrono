package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

class CancelAccountMigration @Inject constructor(
    private val repo: AuthRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CancelAccountMigration.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        when(params) {
            is Params.Current -> {
                val acc = repo.getCurrentAccount()
                repo.cancelAccountMigration(
                    account = acc.id
                )
            }
            is Params.Other -> {
                repo.cancelAccountMigration(
                    account = params.acc
                )
            }
        }
    }

    sealed class Params {
        data object Current : Params()
        data class Other(val acc: Id) : Params()
    }
}