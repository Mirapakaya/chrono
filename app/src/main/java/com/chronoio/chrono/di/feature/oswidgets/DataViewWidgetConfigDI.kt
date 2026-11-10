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
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.device.PathProvider
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.`object`.GetObject
import com.chronoio.chrono.domain.platform.InitialParamsProvider
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.feature_os_widgets.persistence.DataViewItemsFetcher
import com.chronoio.chrono.feature_os_widgets.persistence.OsWidgetDataViewEntity
import com.chronoio.chrono.feature_os_widgets.persistence.OsWidgetsDataStore
import com.chronoio.chrono.feature_os_widgets.presentation.DataViewWidgetConfigStore
import com.chronoio.chrono.feature_os_widgets.presentation.DataViewWidgetUpdater
import com.chronoio.chrono.feature_os_widgets.presentation.DataViewWidgetConfigViewModel
import com.chronoio.chrono.feature_os_widgets.ui.OsDataViewWidgetUpdater
import com.chronoio.chrono.ui.oswidgets.DataViewWidgetConfigActivity
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [DataViewWidgetConfigDependencies::class],
    modules = [DataViewWidgetConfigModule::class]
)
@PerScreen
interface DataViewWidgetConfigComponent {

    fun inject(activity: DataViewWidgetConfigActivity)

    @Component.Factory
    interface Factory {
        fun create(dependencies: DataViewWidgetConfigDependencies): DataViewWidgetConfigComponent
    }
}

@Module
object DataViewWidgetConfigModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDataStore(context: Context): DataViewWidgetConfigStore {
        val appContext = context.applicationContext
        val dataStore = OsWidgetsDataStore(appContext)
        return object : DataViewWidgetConfigStore {
            override suspend fun save(config: OsWidgetDataViewEntity) {
                dataStore.saveDataViewConfig(config)
            }
        }
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDataViewWidgetUpdater(context: Context): DataViewWidgetUpdater {
        val appContext = context.applicationContext
        return DataViewWidgetUpdater { appWidgetId ->
            OsDataViewWidgetUpdater.update(appContext, appWidgetId)
        }
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDataViewItemsFetcher(
        getObject: GetObject,
        searchObjects: SearchObjects,
        blockRepository: BlockRepository
    ): DataViewItemsFetcher = DataViewItemsFetcher(
        getObject = getObject,
        searchObjects = searchObjects,
        blockRepository = blockRepository
    )

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
    fun provideDataViewWidgetViewModelFactory(
        spaceViews: SpaceViewSubscriptionContainer,
        urlBuilder: UrlBuilder,
        searchObjects: SearchObjects,
        getObject: GetObject,
        dataStore: DataViewWidgetConfigStore,
        itemsFetcher: DataViewItemsFetcher,
        widgetUpdater: DataViewWidgetUpdater,
        launchWallet: LaunchWallet,
        launchAccount: LaunchAccount
    ): DataViewWidgetConfigViewModel.Factory = DataViewWidgetConfigViewModel.Factory(
        spaceViews = spaceViews,
        urlBuilder = urlBuilder,
        searchObjects = searchObjects,
        getObject = getObject,
        dataStore = dataStore,
        itemsFetcher = itemsFetcher,
        widgetUpdater = widgetUpdater,
        launchWallet = launchWallet,
        launchAccount = launchAccount
    )
}

interface DataViewWidgetConfigDependencies : ComponentDependencies {
    fun context(): Context
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun urlBuilder(): UrlBuilder
    fun searchObjects(): SearchObjects
    fun blockRepository(): BlockRepository
    fun userSettingsRepository(): UserSettingsRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun authRepository(): AuthRepository
    fun pathProvider(): PathProvider
    fun configStorage(): ConfigStorage
    fun spaceManager(): SpaceManager
    fun metricsProvider(): InitialParamsProvider
    fun awaitAccountStartManager(): AwaitAccountStartManager
    fun preferredSpaceIdHolder(): PreferredSpaceIdHolder
}
