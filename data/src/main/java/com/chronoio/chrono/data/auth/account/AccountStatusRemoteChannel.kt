package com.chronoio.chrono.data.auth.account

import com.chronoio.chrono.core_models.AccountStatus
import kotlinx.coroutines.flow.Flow

interface AccountStatusRemoteChannel {
    fun observe(): Flow<AccountStatus>
}