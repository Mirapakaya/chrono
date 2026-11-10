package com.chronoio.chrono.domain.notifications

import com.chronoio.chrono.core_models.DecryptedPushContent
import com.chronoio.chrono.core_models.Id

interface NotificationBuilder {
    suspend fun buildAndNotify(message: DecryptedPushContent.Message, spaceId: Id, groupId: String)
    fun clearNotificationChannel(spaceId: String, chatId: String)
    fun clearNotificationsByGroupId(groupId: String)
}