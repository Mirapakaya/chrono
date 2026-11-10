package com.chronoio.chrono.domain.search

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.SubscriptionEvent
import kotlinx.coroutines.flow.Flow

/**
 * Channel for events related to changes and transformations of objects.
 * @see SubscriptionEvent
 */
interface SubscriptionEventChannel {
    fun subscribe(subscriptions: List<Id>): Flow<List<SubscriptionEvent>>
}