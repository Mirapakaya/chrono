package com.chronoio.chrono.data.auth.event

import com.chronoio.chrono.core_models.Notification
import com.chronoio.chrono.domain.workspace.NotificationsChannel
import kotlinx.coroutines.flow.Flow

interface NotificationsRemoteChannel {
    fun observe(): Flow<List<Notification.Event>>
}

class NotificationsDateChannel(
    private val channel: NotificationsRemoteChannel
) : NotificationsChannel {

    override fun observe(): Flow<List<Notification.Event>> {
        return channel.observe()
    }
}