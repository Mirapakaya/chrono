package com.chronoio.chrono.domain.library.processors

import com.chronoio.chrono.core_models.SubscriptionEvent
import com.chronoio.chrono.domain.library.SubscriptionObject

class EventRemoveProcessor : SubscriptionEventProcessor<SubscriptionEvent.Remove> {

    override fun process(
        event: SubscriptionEvent.Remove,
        dataItems: MutableList<SubscriptionObject>
    ): MutableList<SubscriptionObject> = with(dataItems) {
        retainAll {
            it.id != event.target
        }
        return this
    }

}