package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.core_models.Account
import com.chronoio.chrono.domain.auth.model.AuthStatus
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

/**
 * Use case for checking authorisation status.
 * User can be either authorized or unauthorized based on number of available accounts.
 * @param repository repository containing information about available accounts
 */
class CheckAuthorizationStatus @Inject constructor(
    private val repository: AuthRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, Pair<AuthStatus, Account?>>(dispatchers.io) {

    override suspend fun doWork(params: Unit): Pair<AuthStatus, Account?> {
        return repository.getAccounts().let { accounts ->
            val mnemonic = repository.getMnemonic()
            if (accounts.isNotEmpty() && !mnemonic.isNullOrBlank())
                AuthStatus.AUTHORIZED to accounts.firstOrNull()
            else
                AuthStatus.UNAUTHORIZED to null
        }
    }
}