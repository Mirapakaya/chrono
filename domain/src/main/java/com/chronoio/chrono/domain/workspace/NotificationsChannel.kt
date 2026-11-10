package com.chronoio.chrono.domain.workspace

import com.chronoio.chrono.core_models.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationsChannel {
    fun observe(): Flow<List<Notification.Event>>
}