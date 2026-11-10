package com.chronoio.chrono.di.feature.spaces

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.core_utils.notifications.NotificationPermissionManager
import com.chronoio.chrono.device.DefaultGradientCollectionProvider
import com.chronoio.chrono.device.share.debug.DebugSpaceDeviceFileContentSaver
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.interactor.GetAccount
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.clipboard.Clipboard
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.cover.GetCoverGradientCollection
import com.chronoio.chrono.domain.debugging.DebugSpaceContentSaver
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.device.DeviceTokenStoringService
import com.chronoio.chrono.domain.invite.SpaceInviteLinkStore
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.AppActionManager
import com.chronoio.chrono.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.notifications.SetSpaceNotificationMode
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.ProfileSubscriptionManager
import com.chronoio.chrono.domain.wallpaper.SetWallpaper
import com.chronoio.chrono.domain.wallpaper.WallpaperStore
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.spaces.SpaceGradientProvider
import com.chronoio.chrono.presentation.spaces.SpaceSettingsViewModel
import com.chronoio.chrono.presentation.util.downloader.UriFileProvider
import com.chronoio.chrono.ui.settings.space.SpaceSettingsFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [SpaceSettingsDependencies::class],
    modules = [
        SpaceSettingsModule::class,
        SpaceSettingsModule.Bindings::class
    ]
)
@PerScreen
interface SpaceSettingsComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: SpaceSettingsViewModel.VmParams,
            dependencies: SpaceSettingsDependencies
        ) : SpaceSettingsComponent
    }

    fun inject(fragment: SpaceSettingsFragment)
}

@Module
object SpaceSettingsModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSpaceGradientProvider(): SpaceGradientProvider = SpaceGradientProvider.Default

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetAccountUseCase(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetAccount = GetAccount(repo = repo, dispatcher = dispatchers)

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
    fun provideSetWallpaper(
        repo: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ) : SetWallpaper = SetWallpaper(
        repo = repo,
        store = WallpaperStore.Default,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetCoverGradientCollectionUseCase(
    ): GetCoverGradientCollection = GetCoverGradientCollection(DefaultGradientCollectionProvider)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSearchObjects(
        repository: BlockRepository
    ): SearchObjects = SearchObjects(repo = repository)

    @Module
    interface Bindings {

        @PerScreen
        @Binds
        fun bindSpaceDebugDeviceSharer(
            saver: DebugSpaceDeviceFileContentSaver
        ): DebugSpaceContentSaver
        @PerScreen
        @Binds
        fun factory(factory: SpaceSettingsViewModel.Factory): ViewModelProvider.Factory
    }
}

interface SpaceSettingsDependencies : ComponentDependencies {
    fun blockRepo(): BlockRepository
    fun auth(): AuthRepository
    fun appActions(): AppActionManager
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun settings(): UserSettingsRepository
    fun urlBuilder(): UrlBuilder
    fun analytics(): Analytics
    fun dispatchers(): AppCoroutineDispatchers
    fun spaceManager(): SpaceManager
    fun container(): StorelessSubscriptionContainer
    fun context(): Context
    fun userPermission(): UserPermissionProvider
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun activeSpaceMemberSubscriptionContainer(): ActiveSpaceMemberSubscriptionContainer
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun profileContainer(): ProfileSubscriptionManager
    fun uriFileProvider(): UriFileProvider
    fun notificationsPermissionManager(): NotificationPermissionManager
    fun deviceTokenStoreService(): DeviceTokenStoringService
    fun clipboard() : Clipboard
    fun logger(): Logger
    fun spaceInviteLinkStore() : SpaceInviteLinkStore
    fun stringResourceProvider(): StringResourceProvider
    fun fieldParser(): FieldParser
}