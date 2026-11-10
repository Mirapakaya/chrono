package com.chronoio.chrono.data.auth.event

import com.chronoio.chrono.core_models.Event
import kotlinx.coroutines.flow.Flow

interface EventRemoteChannel {
    fun observeEvents(context: String? = null): Flow<List<Event>>
}