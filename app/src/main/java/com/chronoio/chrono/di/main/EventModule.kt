package com.chronoio.chrono.di.main

import com.chronoio.chrono.core_utils.tools.FeatureToggles
import com.chronoio.chrono.data.auth.account.AccountStatusDataChannel
import com.chronoio.chrono.data.auth.account.AccountStatusRemoteChannel
import com.chronoio.chrono.data.auth.event.ChatEventRemoteChannel
import com.chronoio.chrono.data.auth.event.EventDataChannel
import com.chronoio.chrono.data.auth.event.EventRemoteChannel
import com.chronoio.chrono.data.auth.event.FileLimitsDataChannel
import com.chronoio.chrono.data.auth.event.FileLimitsRemoteChannel
import com.chronoio.chrono.data.auth.event.SubscriptionDataChannel
import com.chronoio.chrono.data.auth.event.SubscriptionEventRemoteChannel
import com.chronoio.chrono.data.auth.status.SyncAndP2PStatusEventsStore
import com.chronoio.chrono.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import com.chronoio.chrono.domain.account.AccountStatusChannel
import com.chronoio.chrono.domain.chats.ChatEventChannel
import com.chronoio.chrono.domain.event.interactor.EventChannel
import com.chronoio.chrono.domain.search.SubscriptionEventChannel
import com.chronoio.chrono.domain.workspace.FileLimitsEventChannel
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.middleware.interactor.AccountStatusMiddlewareChannel
import com.chronoio.chrono.middleware.interactor.DefaultMiddlewareEventRegistrar
import com.chronoio.chrono.middleware.interactor.EventHandler
import com.chronoio.chrono.middleware.interactor.EventHandlerChannel
import com.chronoio.chrono.middleware.interactor.EventHandlerChannelImpl
import com.chronoio.chrono.middleware.interactor.FileLimitsMiddlewareChannel
import com.chronoio.chrono.middleware.interactor.MiddlewareEventChannel
import com.chronoio.chrono.middleware.interactor.MiddlewareEventRegistrar
import com.chronoio.chrono.middleware.interactor.MiddlewareProtobufLogger
import com.chronoio.chrono.middleware.interactor.MiddlewareSubscriptionEventChannel
import com.chronoio.chrono.middleware.interactor.SyncAndP2PStatusEventsStoreImpl
import com.chronoio.chrono.middleware.interactor.events.ChatEventMiddlewareChannel
import com.chronoio.chrono.presentation.common.PayloadDelegator
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module(includes = [EventModule.Bindings::class])
object EventModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideEventChannel(
        channel: EventDataChannel
    ): EventChannel = channel

    @JvmStatic
    @Provides
    @Singleton
    fun provideEventDataChannel(
        remote: EventRemoteChannel
    ): EventDataChannel = EventDataChannel(remote)

    @JvmStatic
    @Provides
    @Singleton
    fun provideEventRemoteChannel(
        proxy: EventProxy,
        featureToggles: FeatureToggles
    ): EventRemoteChannel = MiddlewareEventChannel(events = proxy, featureToggles = featureToggles)

    @JvmStatic
    @Provides
    @Singleton
    fun provideAccountStatusChannel(
        channel: AccountStatusDataChannel
    ) : AccountStatusChannel = channel

    @JvmStatic
    @Provides
    @Singleton
    fun provideAccountStatusDataChannel(
        channel: AccountStatusRemoteChannel
    ) = AccountStatusDataChannel(
        remote = channel
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideAccountStatusRemoteChannel(
        proxy: EventProxy
    ) : AccountStatusRemoteChannel = AccountStatusMiddlewareChannel(
        events = proxy
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideSubscriptionEventChannel(
        channel: SubscriptionDataChannel
    ): SubscriptionEventChannel = channel

    @JvmStatic
    @Provides
    @Singleton
    fun provideSubscriptionEventDataChannel(
        remote: SubscriptionEventRemoteChannel
    ): SubscriptionDataChannel = SubscriptionDataChannel(remote)

    @JvmStatic
    @Provides
    @Singleton
    fun provideSubscriptionEventRemoteChannel(
        proxy: EventProxy,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): SubscriptionEventRemoteChannel = MiddlewareSubscriptionEventChannel(
        events = proxy,
        scope = scope
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideFileLimitsRemoteChannel(
        proxy: EventProxy
    ): FileLimitsRemoteChannel = FileLimitsMiddlewareChannel(events = proxy)

    @JvmStatic
    @Provides
    @Singleton
    fun provideFileLimitsDataChannel(
        channel: FileLimitsRemoteChannel
    ): FileLimitsEventChannel = FileLimitsDataChannel(channel = channel)

    @JvmStatic
    @Provides
    @Singleton
    fun provideEventHandler(
        logger: MiddlewareProtobufLogger,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        channel: EventHandlerChannel,
        syncP2PStore: SyncAndP2PStatusEventsStore,
        registrar: MiddlewareEventRegistrar
    ): EventProxy = EventHandler(
        scope = scope,
        logger = logger,
        channel = channel,
        syncP2PStore = syncP2PStore,
        registrar = registrar
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideMiddlewareEventRegistrar(): MiddlewareEventRegistrar = DefaultMiddlewareEventRegistrar()

    @JvmStatic
    @Provides
    @Singleton
    fun provideSyncAndP2PStatusEventsSubscription(
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        channel: EventHandlerChannel
    ): SyncAndP2PStatusEventsStore = SyncAndP2PStatusEventsStoreImpl(
        scope = scope,
        channel = channel
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideDefaultEventChannel(): EventHandlerChannel = EventHandlerChannelImpl()

    //region Chats

    @JvmStatic
    @Provides
    @Singleton
    fun provideChatEventChannel(
        channel: ChatEventRemoteChannel.Default
    ): ChatEventChannel = channel

    @JvmStatic
    @Provides
    @Singleton
    fun provideChatEventDataChannel(
        remote: ChatEventRemoteChannel
    ): ChatEventRemoteChannel.Default = ChatEventRemoteChannel.Default(
        channel = remote
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideChatEventMWChannel(
        proxy: EventProxy
    ): ChatEventRemoteChannel = ChatEventMiddlewareChannel(
        proxy
    )

    //endregion

    @Module
    interface Bindings {

        @Binds
        @Singleton
        fun payloadDelegator(default: PayloadDelegator.Default): PayloadDelegator
    }
}