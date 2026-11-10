package com.chronoio.chrono.domain.library.processors

import com.chronoio.chrono.core_models.SubscriptionEvent
import com.chronoio.chrono.domain.library.SubscriptionObject
import com.chronoio.chrono.domain.`object`.unset

class EventUnsetProcessor : SubscriptionEventProcessor<SubscriptionEvent.Unset> {

    override fun process(
        event: SubscriptionEvent.Unset,
        dataItems: MutableList<SubscriptionObject>
    ): MutableList<SubscriptionObject> = with(dataItems) {
        for (index in indices) {
            val item = get(index)
            if (item.id == event.target) {
                set(index, SubscriptionObject(item.id, item.objectWrapper?.unset(event.keys)))
            }
        }
        return this
    }

}