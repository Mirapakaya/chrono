package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import javax.inject.Inject

/**
 * Use case for recovering wallet via recovery (aka keychain) phrase (mnemonic).
 */
class RecoverWallet @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<Unit, RecoverWallet.Params>() {

    override suspend fun run(params: Params) = try {
        repository.recoverWallet(
            path = params.path,
            mnemonic = params.mnemonic
        ).let {
            Either.Right(it)
        }
    } catch (e: Throwable) {
        Either.Left(e)
    }

    class Params(val path: String, val mnemonic: String)
}