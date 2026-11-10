package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import javax.inject.Inject

/**
 * Use case for starting loading remote accounts associated with current wallet.
 */
class StartLoadingAccounts @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<Unit, StartLoadingAccounts.Params>() {

    override suspend fun run(params: Params) = try {
        repository.startLoadingAccounts().let {
            Either.Right(it)
        }
    } catch (e: Throwable) {
        Either.Left(e)
    }

    class Params
}