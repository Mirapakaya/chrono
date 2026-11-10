package com.chronoio.chrono.domain.account

import com.chronoio.chrono.core_models.AccountStatus
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.BaseUseCase

class DeleteAccount(private val repo: AuthRepository) : BaseUseCase<AccountStatus, BaseUseCase.None>() {
    override suspend fun run(params: None) = safe { repo.deleteAccount() }
}