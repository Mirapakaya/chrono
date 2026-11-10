package com.chronoio.chrono.data.auth.event

import com.chronoio.chrono.core_models.Event
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.event.interactor.EventChannel
import kotlinx.coroutines.flow.Flow

class EventDataChannel(private val remote: EventRemoteChannel) : EventChannel {

    override fun observeEvents(context: Id?): Flow<List<Event>> = remote.observeEvents(context)

}