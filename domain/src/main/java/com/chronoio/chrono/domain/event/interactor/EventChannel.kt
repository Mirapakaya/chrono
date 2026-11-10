package com.chronoio.chrono.domain.event.interactor

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Event
import kotlinx.coroutines.flow.Flow

interface EventChannel {
    fun observeEvents(context: Id? = null): Flow<List<Event>>
}