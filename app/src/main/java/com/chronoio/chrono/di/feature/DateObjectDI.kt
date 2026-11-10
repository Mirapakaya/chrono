package com.chronoio.chrono.di.feature

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.GetObject
import com.chronoio.chrono.domain.`object`.SetObjectDetails
import com.chronoio.chrono.domain.objects.GetDateObjectByTimestamp
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.relations.GetObjectRelationListById
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.search.SubscriptionEventChannel
import com.chronoio.chrono.domain.spaces.ClearLastOpenedSpace
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.feature_date.viewmodel.DateObjectVMFactory
import com.chronoio.chrono.feature_date.viewmodel.DateObjectVmParams
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.navigation.backstack.BackHistoryDelegate
import com.chronoio.chrono.presentation.navigation.backstack.NavigationBackStackInspector
import com.chronoio.chrono.presentation.vault.ExitToVaultDelegate
import com.chronoio.chrono.ui.date.DateObjectFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [DateObjectDependencies::class],
    modules = [
        DateObjectModule::class,
        DateObjectModule.Declarations::class
    ]
)
@PerScreen
interface DateObjectComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: DateObjectVmParams,
            dependencies: DateObjectDependencies
        ): DateObjectComponent
    }

    fun inject(fragment: DateObjectFragment)
}

@Module
object DateObjectModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun getObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        settings: UserSettingsRepository,
    ): GetObject = GetObject(
        repo = repo,
        dispatchers = dispatchers,
        settings = settings
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideStoreLessSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): StorelessSubscriptionContainer = StorelessSubscriptionContainer.Impl(
        repo = repo,
        channel = channel,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun searchObjects(
        repo: BlockRepository
    ): SearchObjects = SearchObjects(repo = repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun createObject(
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
    fun provideGetDefaultPageType(
        userSettingsRepository: UserSettingsRepository,
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = userSettingsRepository,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRelationListWithValue(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetObjectRelationListById = GetObjectRelationListById(repository, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateDetailUseCase(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(repository, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun getSetObjectListIsArchived(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBackHistoryDelegate(
        inspector: NavigationBackStackInspector,
        searchObjects: SearchObjects,
        fieldParser: FieldParser,
        dispatchers: AppCoroutineDispatchers
    ): BackHistoryDelegate = BackHistoryDelegate.Default(
        inspector = inspector,
        searchObjects = searchObjects,
        fieldParser = fieldParser,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideExitToVaultDelegate(
        spaceManager: SpaceManager,
        clearLastOpenedSpace: ClearLastOpenedSpace
    ): ExitToVaultDelegate = ExitToVaultDelegate.Default(
        spaceManager = spaceManager,
        clearLastOpenedSpace = clearLastOpenedSpace
    )

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: DateObjectVMFactory
        ): ViewModelProvider.Factory

    }
}

interface DateObjectDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun subEventChannel(): SubscriptionEventChannel
    fun logger(): Logger
    fun localeProvider(): LocaleProvider
    fun config(): ConfigStorage
    fun userPermissionProvider(): UserPermissionProvider
    fun provideStoreOfRelations(): StoreOfRelations
    fun provideDateProvider(): DateProvider
    fun provideSpaceSyncAndP2PStatusProvider(): SpaceSyncAndP2PStatusProvider
    fun provideUserSettingsRepository(): UserSettingsRepository
    fun fieldParser(): FieldParser
    fun provideGetDateObjectByTimestamp(): GetDateObjectByTimestamp
    fun provideSpaceViews(): SpaceViewSubscriptionContainer
    fun spaceManager(): SpaceManager
    fun navigationBackStackInspector(): NavigationBackStackInspector
}