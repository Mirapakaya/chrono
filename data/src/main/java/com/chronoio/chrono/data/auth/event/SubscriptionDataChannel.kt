package com.chronoio.chrono.data.auth.event

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.search.SubscriptionEventChannel

class SubscriptionDataChannel(
    private val remote: SubscriptionEventRemoteChannel
) : SubscriptionEventChannel {
    override fun subscribe(subscriptions: List<Id>) = remote.subscribe(subscriptions)
}