package com.chronoio.chrono.domain.chats

import com.chronoio.chrono.core_models.Event
import com.chronoio.chrono.core_models.Id
import kotlinx.coroutines.flow.Flow

interface ChatEventChannel {
    fun observe(chat: Id): Flow<List<Event.Command.Chats>>
    fun subscribe(subscribe: Id): Flow<List<Event.Command.Chats>>
}