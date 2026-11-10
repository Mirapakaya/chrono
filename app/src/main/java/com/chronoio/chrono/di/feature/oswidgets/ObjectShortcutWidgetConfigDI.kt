package com.chronoio.chrono.di.feature.oswidgets

import android.content.Context
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.interactor.LaunchAccount
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.auth.interactor.LaunchWallet
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.device.PathProvider
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.platform.InitialParamsProvider
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.feature_os_widgets.persistence.OsWidgetIconCache
import com.chronoio.chrono.feature_os_widgets.persistence.OsWidgetObjectShortcutEntity
import com.chronoio.chrono.feature_os_widgets.persistence.OsWidgetsDataStore
import com.chronoio.chrono.feature_os_widgets.presentation.ObjectShortcutIconCache
import com.chronoio.chrono.feature_os_widgets.presentation.ObjectShortcutWidgetConfigStore
import com.chronoio.chrono.feature_os_widgets.presentation.ObjectShortcutWidgetConfigViewModel
import com.chronoio.chrono.feature_os_widgets.presentation.ObjectShortcutWidgetUpdater
import com.chronoio.chrono.feature_os_widgets.ui.OsObjectShortcutWidgetUpdater
import com.chronoio.chrono.ui.oswidgets.ObjectShortcutWidgetConfigActivity
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [ObjectShortcutWidgetConfigDependencies::class],
    modules = [ObjectShortcutWidgetConfigModule::class]
)
@PerScreen
interface ObjectShortcutWidgetConfigComponent {

    fun inject(activity: ObjectShortcutWidgetConfigActivity)

    @Component.Factory
    interface Factory {
        fun create(dependencies: ObjectShortcutWidgetConfigDependencies): ObjectShortcutWidgetConfigComponent
    }
}

@Module
object ObjectShortcutWidgetConfigModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectShortcutStore(context: Context): ObjectShortcutWidgetConfigStore {
        val appContext = context.applicationContext
        val dataStore = OsWidgetsDataStore(appContext)
        return object : ObjectShortcutWidgetConfigStore {
            override suspend fun save(config: OsWidgetObjectShortcutEntity) {
                dataStore.saveObjectShortcutConfig(config)
            }
        }
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectShortcutIconCache(context: Context): ObjectShortcutIconCache {
        val appContext = context.applicationContext
        val iconCache = OsWidgetIconCache(appContext)
        return object : ObjectShortcutIconCache {
            override suspend fun cacheForWidget(url: String, appWidgetId: Int): String? {
                return iconCache.cacheShortcutIcon(
                    url = url,
                    widgetId = appWidgetId,
                    prefix = OsWidgetIconCache.PREFIX_OBJECT
                )
            }
        }
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectShortcutWidgetUpdater(context: Context): ObjectShortcutWidgetUpdater {
        val appContext = context.applicationContext
        return ObjectShortcutWidgetUpdater { appWidgetId ->
            OsObjectShortcutWidgetUpdater.update(appContext, appWidgetId)
        }
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideLaunchWallet(
        authRepository: AuthRepository,
        pathProvider: PathProvider
    ): LaunchWallet = LaunchWallet(
        repository = authRepository,
        pathProvider = pathProvider
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideLaunchAccount(
        authRepository: AuthRepository,
        pathProvider: PathProvider,
        configStorage: ConfigStorage,
        spaceManager: SpaceManager,
        initialParamsProvider: InitialParamsProvider,
        userSettings: UserSettingsRepository,
        awaitAccountStartManager: AwaitAccountStartManager,
        preferredSpaceIdHolder: PreferredSpaceIdHolder
    ): LaunchAccount = LaunchAccount(
        repository = authRepository,
        pathProvider = pathProvider,
        configStorage = configStorage,
        spaceManager = spaceManager,
        initialParamsProvider = initialParamsProvider,
        settings = userSettings,
        awaitAccountStartManager = awaitAccountStartManager,
        preferredSpaceIdHolder = preferredSpaceIdHolder
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectShortcutViewModelFactory(
        spaceViews: SpaceViewSubscriptionContainer,
        urlBuilder: UrlBuilder,
        searchObjects: SearchObjects,
        configStore: ObjectShortcutWidgetConfigStore,
        iconCache: ObjectShortcutIconCache,
        widgetUpdater: ObjectShortcutWidgetUpdater,
        launchWallet: LaunchWallet,
        launchAccount: LaunchAccount
    ): ObjectShortcutWidgetConfigViewModel.Factory = ObjectShortcutWidgetConfigViewModel.Factory(
        spaceViews = spaceViews,
        urlBuilder = urlBuilder,
        searchObjects = searchObjects,
        configStore = configStore,
        iconCache = iconCache,
        widgetUpdater = widgetUpdater,
        launchWallet = launchWallet,
        launchAccount = launchAccount
    )
}

interface ObjectShortcutWidgetConfigDependencies : ComponentDependencies {
    fun context(): Context
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun urlBuilder(): UrlBuilder
    fun searchObjects(): SearchObjects
    fun authRepository(): AuthRepository
    fun pathProvider(): PathProvider
    fun configStorage(): ConfigStorage
    fun spaceManager(): SpaceManager
    fun metricsProvider(): InitialParamsProvider
    fun userSettingsRepository(): UserSettingsRepository
    fun awaitAccountStartManager(): AwaitAccountStartManager
    fun preferredSpaceIdHolder(): PreferredSpaceIdHolder
}
