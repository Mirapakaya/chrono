package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.device.PathProvider
import kotlinx.coroutines.withTimeout

/**
 * Sets current wallet for current application session.
 */
class LaunchWallet(
    private val repository: AuthRepository,
    private val pathProvider: PathProvider
) : BaseUseCase<Unit, BaseUseCase.None>() {

    override suspend fun run(params: None) = try {
        withTimeout(TIMEOUT_DURATION) {
            val mnemonic = repository.getMnemonic()
            if (mnemonic.isNullOrBlank()) throw IllegalStateException("Mnemonic is empty")
            repository.recoverWallet(
                mnemonic = mnemonic,
                path = pathProvider.providePath()
            ).let {
                Either.Right(it)
            }
        }
    } catch (e: Throwable) {
        Either.Left(e)
    }

    companion object {
        const val TIMEOUT_DURATION = 10000L
    }
}