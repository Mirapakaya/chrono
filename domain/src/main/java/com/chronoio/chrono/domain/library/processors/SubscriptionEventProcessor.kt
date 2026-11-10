package com.chronoio.chrono.domain.library.processors

import com.chronoio.chrono.core_models.SubscriptionEvent
import com.chronoio.chrono.domain.library.SubscriptionObject

interface SubscriptionEventProcessor<T: SubscriptionEvent> {
    fun process(
        event: T,
        dataItems: MutableList<SubscriptionObject>
    ): MutableList<SubscriptionObject>
}