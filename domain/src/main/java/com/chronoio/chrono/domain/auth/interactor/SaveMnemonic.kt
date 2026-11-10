package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import javax.inject.Inject

/**
 * Use case for saving current user's mnemonic.
 */
class SaveMnemonic @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<Unit, SaveMnemonic.Params>() {

    override suspend fun run(params: Params) = try {
        repository.saveMnemonic(
            mnemonic = params.mnemonic
        ).let {
            Either.Right(it)
        }
    } catch (e: Throwable) {
        Either.Left(e)
    }

    class Params(val mnemonic: String)
}