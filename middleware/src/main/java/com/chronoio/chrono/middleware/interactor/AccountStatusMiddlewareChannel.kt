package com.chronoio.chrono.middleware.interactor
import com.chronoio.chrono.middleware.EventGroup

import com.chronoio.chrono.core_models.AccountStatus
import com.chronoio.chrono.data.auth.account.AccountStatusRemoteChannel
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.middleware.mappers.core
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class AccountStatusMiddlewareChannel(
    private val events: EventProxy
) : AccountStatusRemoteChannel {

    override fun observe(): Flow<AccountStatus> = events.flow(EventGroup.ACCOUNT).mapNotNull { e ->
        val updates = e.messages.filter { m ->
            m.accountUpdate != null
        }
        val lastUpdate = updates.lastOrNull()?.accountUpdate

        lastUpdate?.status?.core()
    }
}