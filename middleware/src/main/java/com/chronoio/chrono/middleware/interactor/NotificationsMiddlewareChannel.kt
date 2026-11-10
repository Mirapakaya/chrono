package com.chronoio.chrono.middleware.interactor
import com.chronoio.chrono.middleware.EventGroup

import com.chronoio.chrono.core_models.Notification
import com.chronoio.chrono.data.auth.event.NotificationsRemoteChannel
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.middleware.mappers.toCoreModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull

class NotificationsMiddlewareChannel(
    private val eventsProxy: EventProxy
) : NotificationsRemoteChannel {

    override fun observe(): Flow<List<Notification.Event>> {
        return eventsProxy.flow(EventGroup.NOTIFICATIONS)
            .mapNotNull { emission ->
                emission.messages.mapNotNull { message ->
                    when {
                        message.notificationUpdate != null -> {
                            val event = message.notificationUpdate
                            checkNotNull(event)
                            Notification.Event.Update(
                                notification = event.notification?.toCoreModel()
                            )
                        }
                        message.notificationSend != null -> {
                            val event = message.notificationSend
                            checkNotNull(event)
                            Notification.Event.Send(
                                notification = event.notification?.toCoreModel()
                            )
                        }
                        else -> null
                    }
                }
            }.filter { events -> events.isNotEmpty() }
    }
}