package com.chronoio.chrono.domain.account

import com.chronoio.chrono.core_models.AccountStatus
import kotlinx.coroutines.flow.Flow

interface AccountStatusChannel {
    fun observe(): Flow<AccountStatus>
}