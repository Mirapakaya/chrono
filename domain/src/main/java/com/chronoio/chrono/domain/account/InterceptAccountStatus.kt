package com.chronoio.chrono.domain.account

import com.chronoio.chrono.core_models.AccountStatus
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class InterceptAccountStatus(
    private val channel: AccountStatusChannel,
    private val dispatchers: AppCoroutineDispatchers
) : FlowUseCase<AccountStatus, BaseUseCase.None>() {
    override fun build(params: BaseUseCase.None?): Flow<AccountStatus> {
        return channel.observe().flowOn(dispatchers.io)
    }
}