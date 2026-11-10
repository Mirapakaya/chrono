package com.chronoio.chrono.di.feature

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.CrashReporter
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.core_utils.tools.FeatureToggles
import com.chronoio.chrono.data.auth.event.EventProcessMigrationDateChannel
import com.chronoio.chrono.data.auth.event.EventProcessMigrationRemoteChannel
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.interactor.GetLastOpenedObject
import com.chronoio.chrono.domain.auth.interactor.LaunchAccount
import com.chronoio.chrono.domain.auth.interactor.LaunchWallet
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.device.PathProvider
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.launch.SetDefaultObjectType
import com.chronoio.chrono.domain.deeplink.PendingIntentStore
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.page.CreateObjectByTypeAndTemplate
import com.chronoio.chrono.domain.platform.InitialParamsProvider
import com.chronoio.chrono.domain.search.RelationsSubscriptionManager
import com.chronoio.chrono.domain.spaces.SpaceDeletedStatusWatcher
import com.chronoio.chrono.domain.subscriptions.GlobalSubscriptionManager
import com.chronoio.chrono.domain.templates.GetTemplates
import com.chronoio.chrono.domain.workspace.EventProcessMigrationChannel
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.middleware.interactor.EventProcessMigrationMiddlewareChannel
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.auth.account.MigrationHelperDelegate
import com.chronoio.chrono.presentation.splash.SplashViewModelFactory
import com.chronoio.chrono.ui.splash.SplashFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [SplashDependencies::class],
    modules = [
        SplashModule::class,
        SplashModule.Declarations::class
    ]
)
@PerScreen
interface SplashComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: SplashDependencies): SplashComponent
    }

    fun inject(fragment: SplashFragment)
}

@Module
object SplashModule {

    @JvmStatic
    @PerScreen
    @Provides
    fun provideLaunchAccountUseCase(
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
    @PerScreen
    @Provides
    fun provideLaunchWalletUseCase(
        authRepository: AuthRepository,
        pathProvider: PathProvider
    ): LaunchWallet =
        LaunchWallet(
            repository = authRepository,
            pathProvider = pathProvider
        )

    @JvmStatic
    @Provides
    @PerScreen
    fun getLastOpenedObject(
        repo: BlockRepository,
        userSettingsRepository: UserSettingsRepository
    ): GetLastOpenedObject = GetLastOpenedObject(
        settings = userSettingsRepository,
        blockRepo = repo,
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideGetDefaultPageType(
        userSettingsRepository: UserSettingsRepository,
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
    ): GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = userSettingsRepository,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideSetDefaultPageType(
        repo: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetDefaultObjectType = SetDefaultObjectType(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun getCreateObject(
        repo: BlockRepository,
        getDefaultObjectType: GetDefaultObjectType,
        dispatchers: AppCoroutineDispatchers,
    ): CreateObject = CreateObject(
        repo = repo,
        getDefaultObjectType = getDefaultObjectType,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetTemplates(
        repo: BlockRepository,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers
    ): GetTemplates = GetTemplates(
        repo = repo,
        spaceManager = spaceManager,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSearchObjects(
        repo: BlockRepository
    ): com.chronoio.chrono.domain.search.SearchObjects =
        com.chronoio.chrono.domain.search.SearchObjects(repo = repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateObjectByType(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): CreateObjectByTypeAndTemplate = CreateObjectByTypeAndTemplate(repo, logger, dispatchers)

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: SplashViewModelFactory): ViewModelProvider.Factory

        @Binds
        @PerScreen
        fun bindMigrationHelperDelegate(
            impl: MigrationHelperDelegate.Impl
        ): MigrationHelperDelegate

        @Binds
        @PerScreen
        fun bindMigrationChannel(
            impl: EventProcessMigrationDateChannel
        ): EventProcessMigrationChannel

        @Binds
        @PerScreen
        fun bindMigrationRemoteChannel(
            impl: EventProcessMigrationMiddlewareChannel
        ): EventProcessMigrationRemoteChannel
    }

}

interface SplashDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun urlBuilder(): UrlBuilder
    fun analytics(): Analytics
    fun relationsSubscriptionManager(): RelationsSubscriptionManager
    fun authRepository(): AuthRepository
    fun pathProvider(): PathProvider
    fun featureToggles(): FeatureToggles
    fun configStorage(): ConfigStorage
    fun userSettingsRepository(): UserSettingsRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun crashReporter(): CrashReporter
    fun metricsProvider(): InitialParamsProvider
    fun spaceManager(): SpaceManager
    fun spaceStatusWatcher(): SpaceDeletedStatusWatcher
    fun localeProvider(): LocaleProvider
    fun awaitAccountStartManager(): AwaitAccountStartManager
    fun userPermissionProvider(): UserPermissionProvider
    fun analyticSpaceHelperDelegate(): AnalyticSpaceHelperDelegate
    fun globalSubscriptionManager(): GlobalSubscriptionManager
    fun logger(): Logger
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun eventProxy(): EventProxy
    fun deepLinkResolver(): DeepLinkResolver
    fun pendingIntentStore(): PendingIntentStore
    fun preferredSpaceIdHolder(): PreferredSpaceIdHolder
}
