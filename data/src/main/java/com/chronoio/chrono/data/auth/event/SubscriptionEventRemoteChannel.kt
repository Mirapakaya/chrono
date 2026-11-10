package com.chronoio.chrono.data.auth.event

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.SubscriptionEvent
import kotlinx.coroutines.flow.Flow

interface SubscriptionEventRemoteChannel {
    fun subscribe(subscriptions: List<Id>): Flow<List<SubscriptionEvent>>
}