package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.device.PathProvider
import javax.inject.Inject

class MigrateAccount @Inject constructor(
    private val repo: AuthRepository,
    private val path: PathProvider,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<MigrateAccount.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        when(params) {
            is Params.Current -> {
                val acc = repo.getCurrentAccount()
                val path = path.providePath()
                repo.migrateAccount(
                    account = acc.id,
                    path = path
                )
            }
            is Params.Other -> {
                val path = path.providePath()
                repo.migrateAccount(
                    account = params.acc,
                    path = path
                )
            }
        }
    }

    sealed class Params {
        data object Current : Params()
        data class Other(val acc: Id) : Params()
    }
}

