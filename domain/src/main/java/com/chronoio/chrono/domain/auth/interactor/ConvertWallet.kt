package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import javax.inject.Inject

class ConvertWallet @Inject constructor(
    private val authRepository: AuthRepository
) : BaseUseCase<String, ConvertWallet.Request>() {

    override suspend fun run(params: Request): Either<Throwable, String> = safe {
        val mnemonic = authRepository.convertWallet(entropy = params.entropy)
        authRepository.saveMnemonic(mnemonic)
        mnemonic
    }

    data class Request(val entropy: String)
}