package com.chronoio.chrono.di.main

import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.device.DeviceTokenStoringServiceImpl
import com.chronoio.chrono.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.interactor.sets.GetObjectTypes
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.chats.ChatEventChannel
import com.chronoio.chrono.domain.chats.ChatPreviewContainer
import com.chronoio.chrono.domain.chats.ChatsDetailsSubscriptionContainer
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.debugging.DebugAccountSelectTrace
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.deeplink.PendingIntentStore
import com.chronoio.chrono.domain.device.DeviceTokenStoringService
import com.chronoio.chrono.domain.device.NetworkConnectionStatus
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.library.CrossSpaceSubscriptionContainer
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.DefaultUserPermissionProvider
import com.chronoio.chrono.domain.multiplayer.ParticipantSubscriptionContainer
import com.chronoio.chrono.domain.objects.CrossSpaceObjectTypesContainer
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.notifications.PushKeyProvider
import com.chronoio.chrono.domain.notifications.RegisterDeviceToken
import com.chronoio.chrono.domain.objects.DefaultStoreOfObjectTypes
import com.chronoio.chrono.domain.objects.DefaultStoreOfRelationOptions
import com.chronoio.chrono.domain.objects.DefaultStoreOfRelations
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelationOptions
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.search.HasInstanceOfObjectTypeSubscriptionContainer
import com.chronoio.chrono.domain.search.HasInstanceOfObjectTypeSubscriptionManager
import com.chronoio.chrono.domain.search.ObjectTypesSubscriptionContainer
import com.chronoio.chrono.domain.search.ObjectTypesSubscriptionManager
import com.chronoio.chrono.domain.search.ProfileSubscriptionManager
import com.chronoio.chrono.domain.search.RelationOptionsSubscriptionContainer
import com.chronoio.chrono.domain.search.RelationOptionsSubscriptionManager
import com.chronoio.chrono.domain.search.RelationsSubscriptionContainer
import com.chronoio.chrono.domain.search.RelationsSubscriptionManager
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.search.SubscriptionEventChannel
import com.chronoio.chrono.domain.spaces.SpaceDeletedStatusWatcher
import com.chronoio.chrono.domain.subscriptions.GlobalSubscriptionManager
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.domain.workspace.SyncAndP2PStatusChannel
import com.chronoio.chrono.other.DefaultDeepLinkResolver
import com.chronoio.chrono.presentation.sync.SpaceSyncAndP2PStatusProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module
object SubscriptionsModule {

    @JvmStatic
    @Provides
    @Singleton
    fun relationsSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        store: StoreOfRelations,
        logger: Logger
    ): RelationsSubscriptionContainer = RelationsSubscriptionContainer(
        repo = repo,
        channel = channel,
        store = store,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun objectTypesSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        store: StoreOfObjectTypes,
        logger: Logger
    ): ObjectTypesSubscriptionContainer = ObjectTypesSubscriptionContainer(
        repo = repo,
        channel = channel,
        store = store,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun relationOptionsSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        store: StoreOfRelationOptions,
        logger: Logger
    ): RelationOptionsSubscriptionContainer = RelationOptionsSubscriptionContainer(
        repo = repo,
        channel = channel,
        store = store,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun relationsStore(): StoreOfRelations = DefaultStoreOfRelations()

    @JvmStatic
    @Provides
    @Singleton
    fun objectTypesStore(): StoreOfObjectTypes = DefaultStoreOfObjectTypes()

    @JvmStatic
    @Provides
    @Singleton
    fun relationOptionsStore(): StoreOfRelationOptions = DefaultStoreOfRelationOptions()

    @JvmStatic
    @Provides
    @Singleton
    fun relationsSubscriptionManager(
        subscription: RelationsSubscriptionContainer,
        spaceManager: SpaceManager,
    ): RelationsSubscriptionManager = RelationsSubscriptionManager(
        container = subscription,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @Singleton
    fun objectTypesSubscriptionManager(
        subscription: ObjectTypesSubscriptionContainer,
        spaceManager: SpaceManager
    ): ObjectTypesSubscriptionManager = ObjectTypesSubscriptionManager(
        container = subscription,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @Singleton
    fun hasInstanceOfObjectTypeSubscriptionContainer(
        storelessContainer: StorelessSubscriptionContainer,
        storeOfObjectTypes: StoreOfObjectTypes,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): HasInstanceOfObjectTypeSubscriptionContainer = HasInstanceOfObjectTypeSubscriptionContainer(
        storelessContainer = storelessContainer,
        storeOfObjectTypes = storeOfObjectTypes,
        dispatchers = dispatchers,
        logger = logger,
        scope = scope
    )

    @JvmStatic
    @Provides
    @Singleton
    fun hasInstanceOfObjectTypeSubscriptionManager(
        container: HasInstanceOfObjectTypeSubscriptionContainer,
        spaceManager: SpaceManager
    ): HasInstanceOfObjectTypeSubscriptionManager = HasInstanceOfObjectTypeSubscriptionManager(
        container = container,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @Singleton
    fun relationOptionsSubscriptionManager(
        subscription: RelationOptionsSubscriptionContainer,
        spaceManager: SpaceManager
    ): RelationOptionsSubscriptionManager = RelationOptionsSubscriptionManager(
        container = subscription,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @Singleton
    fun spaceStatusWatcher(
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        configStorage: ConfigStorage,
        container: StorelessSubscriptionContainer,
        logger: Logger
    ): SpaceDeletedStatusWatcher = SpaceDeletedStatusWatcher(
        spaceManager = spaceManager,
        dispatchers = dispatchers,
        configStorage = configStorage,
        scope = scope,
        container = container,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun userPermissionProvider(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: StorelessSubscriptionContainer,
        repo: AuthRepository,
        logger: Logger,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer
    ): UserPermissionProvider = DefaultUserPermissionProvider(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        repo = repo,
        logger = logger,
        spaceViewSubscriptionContainer = spaceViewSubscriptionContainer
    )

    @JvmStatic
    @Provides
    @Singleton
    fun spaceViewSubscriptionContainer(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: StorelessSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager,
        logger: Logger,
        configStorage: ConfigStorage
    ): SpaceViewSubscriptionContainer = SpaceViewSubscriptionContainer.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        awaitAccountStart = awaitAccountStartManager,
        logger = logger,
        config = configStorage
    )

    @JvmStatic
    @Provides
    @Singleton
    fun chatsViewSubscriptionContainer(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: CrossSpaceSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager,
        logger: Logger,
    ): ChatsDetailsSubscriptionContainer = ChatsDetailsSubscriptionContainer.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        awaitAccountStart = awaitAccountStartManager,
        logger = logger,
    )

    @JvmStatic
    @Provides
    @Singleton
    fun participantSubscriptionContainer(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: CrossSpaceSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager,
        logger: Logger,
    ): ParticipantSubscriptionContainer = ParticipantSubscriptionContainer.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        awaitAccountStart = awaitAccountStartManager,
        logger = logger,
    )

    @JvmStatic
    @Provides
    @Singleton
    fun crossSpaceObjectTypesContainer(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: CrossSpaceSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager,
        logger: Logger,
    ): CrossSpaceObjectTypesContainer = CrossSpaceObjectTypesContainer.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        awaitAccountStart = awaitAccountStartManager,
        logger = logger,
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideCrossSpaceSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): CrossSpaceSubscriptionContainer =
        CrossSpaceSubscriptionContainer.Impl(repo, channel, dispatchers, logger)

    @JvmStatic
    @Provides
    @Singleton
    fun provideChatPreviewContainer(
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        dispatchers: AppCoroutineDispatchers,
        repo: BlockRepository,
        logger: Logger,
        events: ChatEventChannel,
        subscription: StorelessSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager
    ): ChatPreviewContainer = ChatPreviewContainer.Default(
        repo = repo,
        dispatchers = dispatchers,
        scope = scope,
        logger = logger,
        events = events,
        subscription = subscription,
        awaitAccountStart = awaitAccountStartManager
    )

    @JvmStatic
    @Provides
    @Singleton
    fun profileSubscriptionManager(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: StorelessSubscriptionContainer,
        awaitAccountStartManager: AwaitAccountStartManager,
        configStorage: ConfigStorage,
        logger: Logger
    ): ProfileSubscriptionManager = ProfileSubscriptionManager.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        awaitAccountStartManager = awaitAccountStartManager,
        configStorage = configStorage,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun activeSpaceMemberSubscriptionContainer(
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        container: StorelessSubscriptionContainer,
        spaceManager: SpaceManager,
        awaitAccountStartManager: AwaitAccountStartManager,
        logger: Logger
    ): ActiveSpaceMemberSubscriptionContainer = ActiveSpaceMemberSubscriptionContainer.Default(
        dispatchers = dispatchers,
        scope = scope,
        container = container,
        manager = spaceManager,
        awaitAccountStart = awaitAccountStartManager,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideSpaceSyncStatusProvider(
        syncChannel: SyncAndP2PStatusChannel,
        spaceManager: SpaceManager
    ): SpaceSyncAndP2PStatusProvider = SpaceSyncAndP2PStatusProviderImpl(
        spaceManager = spaceManager,
        spaceSyncStatusChannel = syncChannel
    )

    @JvmStatic
    @Provides
    @Singleton
    fun globalSubscriptionManager(
        types: ObjectTypesSubscriptionManager,
        relations: RelationsSubscriptionManager,
        relationOptions: RelationOptionsSubscriptionManager,
        permissions: UserPermissionProvider,
        isSpaceDeleted: SpaceDeletedStatusWatcher,
        profileSubscriptionManager: ProfileSubscriptionManager,
        networkConnectionStatus: NetworkConnectionStatus,
        deviceTokenStoringService: DeviceTokenStoringService,
        pushKeyProvider: PushKeyProvider,
        hasInstanceOfType: HasInstanceOfObjectTypeSubscriptionManager,
        logger: Logger
    ): GlobalSubscriptionManager = GlobalSubscriptionManager.Default(
        types = types,
        relations = relations,
        relationOptions = relationOptions,
        permissions = permissions,
        isSpaceDeleted = isSpaceDeleted,
        profile = profileSubscriptionManager,
        networkConnectionStatus = networkConnectionStatus,
        deviceTokenStoringService = deviceTokenStoringService,
        pushKeyProvider = pushKeyProvider,
        hasInstanceOfType = hasInstanceOfType,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun searchObjects(
        repo: BlockRepository
    ): SearchObjects = SearchObjects(repo = repo)

    @JvmStatic
    @PerScreen
    @Singleton
    fun getObjectTypes(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetObjectTypes = GetObjectTypes(repo, dispatchers)

    @JvmStatic
    @Provides
    @Singleton
    fun provideDebugAccountSelectTrace(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DebugAccountSelectTrace =
        DebugAccountSelectTrace(
            repo = repo,
            dispatchers = dispatchers
        )

    @JvmStatic
    @Provides
    @Singleton
    fun deviceTokenStoreService(
        registerDeviceToken: RegisterDeviceToken,
        dispatchers: AppCoroutineDispatchers,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): DeviceTokenStoringService = DeviceTokenStoringServiceImpl(
        registerDeviceToken = registerDeviceToken,
        dispatchers = dispatchers,
        scope = scope
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideDeeplinkResolver() : DeepLinkResolver = DefaultDeepLinkResolver

    @JvmStatic
    @Provides
    @Singleton
    fun providePendingIntentStore(): PendingIntentStore = PendingIntentStore()
}