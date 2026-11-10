package com.chronoio.chrono.di.main

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import com.chronoio.chrono.app.AnytypeNotificationService
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.notifications.NotificationPermissionManager
import com.chronoio.chrono.core_utils.notifications.NotificationPermissionManagerImpl
import com.chronoio.chrono.data.auth.event.NotificationsDateChannel
import com.chronoio.chrono.data.auth.event.NotificationsRemoteChannel
import com.chronoio.chrono.device.NotificationBuilderImpl
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.chats.ChatsDetailsSubscriptionContainer
import com.chronoio.chrono.domain.chats.PushKeyChannel
import com.chronoio.chrono.domain.chats.PushKeySpaceViewChannel
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.notifications.NotificationBuilder
import com.chronoio.chrono.domain.notifications.PushKeyProvider
import com.chronoio.chrono.domain.notifications.SystemNotificationService
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.workspace.NotificationsChannel
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.middleware.interactor.NotificationsMiddlewareChannel
import com.chronoio.chrono.presentation.notifications.NotificationsProvider
import com.chronoio.chrono.presentation.notifications.PushKeyProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

@Module
object NotificationsModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideNotificationsRemoteChannel(
        proxy: EventProxy
    ): NotificationsRemoteChannel = NotificationsMiddlewareChannel(
        eventsProxy = proxy
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideNotificationsChannel(
        channel: NotificationsRemoteChannel
    ): NotificationsChannel = NotificationsDateChannel(
        channel = channel
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideNotificator(
        context: Context
    ): SystemNotificationService = AnytypeNotificationService(context = context)

    @JvmStatic
    @Singleton
    @Provides
    fun provideNotificationsProvider(
        dispatchers: AppCoroutineDispatchers,
        @Named(ConfigModule.DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        awaitAccountStartManager: AwaitAccountStartManager,
        notificationsChannel: NotificationsChannel
    ): NotificationsProvider = NotificationsProvider.Default(
        dispatchers = dispatchers,
        scope = scope,
        notificationsChannel = notificationsChannel,
        awaitAccountStartManager = awaitAccountStartManager,
    )

    @JvmStatic
    @Provides
    @Singleton
    fun providePushKeyProvider(
        @Named("encrypted") sharedPreferences: SharedPreferences,
        dispatchers: AppCoroutineDispatchers,
        @Named(ConfigModule.DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        channel: PushKeyChannel,
        json: Json
    ): PushKeyProvider = PushKeyProviderImpl(
        sharedPreferences = sharedPreferences,
        dispatchers = dispatchers,
        scope = scope,
        channel = channel,
        json = json
    )

    @JvmStatic
    @Provides
    @Singleton
    fun providePushKeyChannel(
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer
    ): PushKeyChannel = PushKeySpaceViewChannel(spaceViewSubscriptionContainer)

    @Provides
    @Singleton
    @JvmStatic
    fun provideNotificationPermissionManager(
        @Named("default") prefs: SharedPreferences,
        context: Context
    ): NotificationPermissionManager {
        return NotificationPermissionManagerImpl(
            sharedPreferences = prefs,
            context = context
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideNotificationBuilder(
        context: Context,
        notificationManager: NotificationManager,
        stringResourceProvider: StringResourceProvider,
        urlBuilder: UrlBuilder,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
        chatsDetailsSubscriptionContainer: ChatsDetailsSubscriptionContainer
    ): NotificationBuilder = NotificationBuilderImpl(
        context = context,
        notificationManager = notificationManager,
        resourceProvider = stringResourceProvider,
        urlBuilder = urlBuilder,
        spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
        chatsDetailsSubscriptionContainer = chatsDetailsSubscriptionContainer
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideNotificationManager(
        context: Context
    ): NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}