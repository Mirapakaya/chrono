package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.core_models.Account
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.FlowUseCase
import javax.inject.Inject

class ObserveAccounts @Inject constructor(
    private val repository: AuthRepository
) : FlowUseCase<Account, Unit>() {

    override fun build(
        params: Unit?
    ) = repository.observeAccounts()
}