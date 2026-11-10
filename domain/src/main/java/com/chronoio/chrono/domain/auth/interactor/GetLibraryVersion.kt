package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either

open class GetLibraryVersion(
    private val repo: AuthRepository
) : BaseUseCase<String, BaseUseCase.None>() {

    override suspend fun run(params: BaseUseCase.None): Either<Throwable, String> = safe {
        repo.getVersion().removePrefix(PREFIX)
    }

    companion object {
        const val PREFIX = "v"
    }
}