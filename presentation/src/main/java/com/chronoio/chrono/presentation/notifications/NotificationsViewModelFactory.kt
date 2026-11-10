package com.chronoio.chrono.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.domain.multiplayer.GetSpaceMemberByIdentity
import com.chronoio.chrono.domain.notifications.ReplyNotifications
import com.chronoio.chrono.domain.spaces.SaveCurrentSpace
import com.chronoio.chrono.domain.workspace.SpaceManager
import javax.inject.Inject

class NotificationsViewModelFactory @Inject constructor(
    private val analytics: Analytics,
    private val notificationsProvider: NotificationsProvider,
    private val spaceManager: SpaceManager,
    private val saveCurrentSpace: SaveCurrentSpace,
    private val getSpaceMemberByIdentity: GetSpaceMemberByIdentity,
    private val replyNotifications: ReplyNotifications,
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotificationsViewModel(
            analytics = analytics,
            notificationsProvider = notificationsProvider,
            spaceManager = spaceManager,
            saveCurrentSpace = saveCurrentSpace,
            getSpaceMemberByIdentity = getSpaceMemberByIdentity,
            replyNotifications = replyNotifications,
        ) as T
    }
}