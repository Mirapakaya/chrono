package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either

/**
 * Use case for retrieving mnemonic associated with current user.
 */
class GetMnemonic(
    private val repository: AuthRepository
) : BaseUseCase<String, Unit>() {

    override suspend fun run(params: Unit) = try {
        repository.getMnemonic().let { mnemonic ->
            if (mnemonic.isNullOrBlank()) throw IllegalStateException("Mnemonic is empty")
            Either.Right(mnemonic)
        }
    } catch (e: Throwable) {
        Either.Left(e)
    }
}