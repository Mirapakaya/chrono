package com.chronoio.chrono.domain.account

import com.chronoio.chrono.core_models.AccountStatus
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.BaseUseCase

class RestoreAccount(
    private val repo: AuthRepository,
    dispatchers: AppCoroutineDispatchers,
) : BaseUseCase<AccountStatus, BaseUseCase.None>(context = dispatchers.io) {

    override suspend fun run(params: None) = safe {
        repo.restoreAccount()
    }
}