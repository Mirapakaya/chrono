package com.chronoio.chrono.di.feature.vault

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.core_utils.notifications.NotificationPermissionManager
import com.chronoio.chrono.core_utils.tools.AppInfo
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.chats.ChatEventChannel
import com.chronoio.chrono.domain.chats.ChatPreviewContainer
import com.chronoio.chrono.domain.chats.ChatsDetailsSubscriptionContainer
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.deeplink.PendingIntentStore
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.AppActionManager
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.domain.multiplayer.ParticipantSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.SearchOneToOneChatByIdentity
import com.chronoio.chrono.domain.multiplayer.SpaceInviteResolver
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.notifications.SetSpaceNotificationMode
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.ProfileSubscriptionManager
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.spaces.CreateSpace
import com.chronoio.chrono.domain.wallpaper.GetSpaceWallpapers
import com.chronoio.chrono.domain.network.NetworkModeProvider
import com.chronoio.chrono.domain.payments.GetMembershipFeatures
import com.chronoio.chrono.domain.widgets.OsWidgetDataViewSync
import com.chronoio.chrono.domain.widgets.OsWidgetSpacesSync
import com.chronoio.chrono.domain.workspace.DeepLinkToObjectDelegate
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.feature_vault.presentation.VaultViewModelFactory
import com.chronoio.chrono.other.DefaultSpaceInviteResolver
import com.chronoio.chrono.ui.vault.VaultFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

@Component(
    dependencies = [VaultComponentDependencies::class],
    modules = [
        VaultModule::class,
        VaultModule.Declarations::class
    ]
)
@PerScreen
interface VaultComponent {

    fun inject(fragment: VaultFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: VaultComponentDependencies): VaultComponent
    }
}

@Module
object VaultModule {
    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: VaultViewModelFactory
        ): ViewModelProvider.Factory

        @PerScreen
        @Binds
        fun deepLinkToObjectDelegate(
            default: DeepLinkToObjectDelegate.Default
        ): DeepLinkToObjectDelegate
    }

    @PerScreen
    @Provides
    fun provideSpaceInviteResolver(): SpaceInviteResolver = DefaultSpaceInviteResolver

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetSpaceNotificationModeUseCase(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetSpaceNotificationMode = SetSpaceNotificationMode(repository, dispatchers)
    
    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetSpaceWallpapers(
        userSettingsRepository: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetSpaceWallpapers = GetSpaceWallpapers(userSettingsRepository, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSearchOneToOneChatByIdentity(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SearchOneToOneChatByIdentity = SearchOneToOneChatByIdentity(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateSpace(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreateSpace = CreateSpace(repo = repository, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetMembershipFeatures(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetMembershipFeatures = GetMembershipFeatures(repo = repository, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSearchObjects(
        repository: BlockRepository
    ): SearchObjects = SearchObjects(repo = repository)
}

interface VaultComponentDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun appCoroutineDispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun userSettingsRepository(): UserSettingsRepository
    fun spaceManager(): SpaceManager
    fun userPermissionProvider(): UserPermissionProvider
    fun auth(): AuthRepository
    fun appActionManager(): AppActionManager
    fun logger(): Logger
    fun awaitAccount(): AwaitAccountStartManager
    fun profileContainer(): ProfileSubscriptionManager
    fun pendingIntentStore(): PendingIntentStore
    fun stringResourceProvider(): StringResourceProvider
    fun dateProvider(): DateProvider
    fun fieldParser(): FieldParser
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun notificationPermissionManager(): NotificationPermissionManager
    fun provideChatEventChannel(): ChatEventChannel
    fun provideStorelessSubscriptionContainer(): StorelessSubscriptionContainer
    fun provideVaultChatPreviewContainer(): ChatPreviewContainer
    fun appInfo(): AppInfo
    @Named(DEFAULT_APP_COROUTINE_SCOPE) fun scope(): CoroutineScope
    fun chatSubscriptionContainer(): ChatsDetailsSubscriptionContainer
    fun participantSubscriptionContainer(): ParticipantSubscriptionContainer
    fun deepLinkResolver(): DeepLinkResolver
    fun configStorage(): ConfigStorage
    fun osWidgetSpacesSync(): OsWidgetSpacesSync
    fun osWidgetDataViewSync(): OsWidgetDataViewSync
    fun networkModeProvider(): NetworkModeProvider
}
