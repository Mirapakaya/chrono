package com.chronoio.chrono.data.auth.event

import com.chronoio.chrono.core_models.Event
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.chats.ChatEventChannel
import kotlinx.coroutines.flow.Flow

interface ChatEventRemoteChannel {
    fun observe(chat: Id): Flow<List<Event.Command.Chats>>
    fun subscribe(subscription: Id): Flow<List<Event.Command.Chats>>
    class Default(
        private val channel: ChatEventRemoteChannel
    ) : ChatEventChannel {

        override fun subscribe(subscribe: Id): Flow<List<Event.Command.Chats>> {
            return channel.subscribe(subscribe)
        }

        override fun observe(chat: Id): Flow<List<Event.Command.Chats>> {
            return channel.observe(chat)
        }
    }
}