package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

/**
 * Use case for creating new wallet.
 */
open class SetupWallet @Inject constructor(
    private val repository: AuthRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetupWallet.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val wallet = repository.createWallet(
            path = params.path
        )
        repository.saveMnemonic(wallet.mnemonic)
    }

    /**
     * @property path repository path.
     */
    class Params(val path: String)
}