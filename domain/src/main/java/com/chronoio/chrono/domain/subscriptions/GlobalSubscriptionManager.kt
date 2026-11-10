package com.chronoio.chrono.domain.subscriptions

import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.device.DeviceTokenStoringService
import com.chronoio.chrono.domain.device.NetworkConnectionStatus
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.notifications.PushKeyProvider
import com.chronoio.chrono.domain.search.HasInstanceOfObjectTypeSubscriptionManager
import com.chronoio.chrono.domain.search.ObjectTypesSubscriptionManager
import com.chronoio.chrono.domain.search.ProfileSubscriptionManager
import com.chronoio.chrono.domain.search.RelationOptionsSubscriptionManager
import com.chronoio.chrono.domain.search.RelationsSubscriptionManager
import com.chronoio.chrono.domain.spaces.SpaceDeletedStatusWatcher
import javax.inject.Inject

interface GlobalSubscriptionManager {

    fun onStart()
    fun onStop()

    class Default @Inject constructor(
        private val types: ObjectTypesSubscriptionManager,
        private val relations: RelationsSubscriptionManager,
        private val relationOptions: RelationOptionsSubscriptionManager,
        private val permissions: UserPermissionProvider,
        private val isSpaceDeleted: SpaceDeletedStatusWatcher,
        private val profile: ProfileSubscriptionManager,
        private val networkConnectionStatus: NetworkConnectionStatus,
        private val deviceTokenStoringService: DeviceTokenStoringService,
        private val pushKeyProvider: PushKeyProvider,
        private val hasInstanceOfType: HasInstanceOfObjectTypeSubscriptionManager,
        private val logger: Logger
    ) : GlobalSubscriptionManager {

        override fun onStart() {
            logger.logInfo("GlobalSubscriptionManager: Starting all subscriptions")
            pushKeyProvider.start()
            types.onStart()
            relations.onStart()
            relationOptions.onStart()
            permissions.start()
            isSpaceDeleted.onStart()
            profile.onStart()
            networkConnectionStatus.start()
            deviceTokenStoringService.start()
            hasInstanceOfType.onStart()
        }

        override fun onStop() {
            logger.logInfo("GlobalSubscriptionManager: Stopping all subscriptions")
            pushKeyProvider.stop()
            types.onStop()
            relations.onStop()
            relationOptions.onStop()
            permissions.stop()
            isSpaceDeleted.onStop()
            profile.onStop()
            networkConnectionStatus.stop()
            deviceTokenStoringService.stop()
            hasInstanceOfType.onStop()
        }
    }

}