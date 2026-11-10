package com.chronoio.chrono.data.auth.account

import com.chronoio.chrono.core_models.AccountStatus
import com.chronoio.chrono.domain.account.AccountStatusChannel
import kotlinx.coroutines.flow.Flow

class AccountStatusDataChannel(
    private val remote: AccountStatusRemoteChannel
) : AccountStatusChannel {
    override fun observe(): Flow<AccountStatus> = remote.observe()
}