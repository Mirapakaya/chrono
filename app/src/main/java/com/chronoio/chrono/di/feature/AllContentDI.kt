package com.chronoio.chrono.di.feature

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.all_content.RestoreAllContentState
import com.chronoio.chrono.domain.all_content.UpdateAllContentState
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.search.SubscriptionEventChannel
import com.chronoio.chrono.domain.workspace.RemoveObjectsFromWorkspace
import com.chronoio.chrono.feature_allcontent.presentation.AllContentViewModel
import com.chronoio.chrono.feature_allcontent.presentation.AllContentViewModelFactory
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.ui.allcontent.AllContentFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [AllContentDependencies::class],
    modules = [
        AllContentModule::class,
        AllContentModule.Declarations::class
    ]
)
@PerScreen
interface AllContentComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: AllContentViewModel.VmParams,
            dependencies: AllContentDependencies
        ): AllContentComponent
    }

    fun inject(fragment: AllContentFragment)
}

@Module
object AllContentModule {

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
    fun provideUpdateAllContentState(
        dispatchers: AppCoroutineDispatchers,
        userSettingsRepository: UserSettingsRepository
    ): UpdateAllContentState = UpdateAllContentState(
        dispatchers = dispatchers,
        settings = userSettingsRepository
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRestoreAllContentState(
        dispatchers: AppCoroutineDispatchers,
        userSettingsRepository: UserSettingsRepository
    ): RestoreAllContentState = RestoreAllContentState(
        dispatchers = dispatchers,
        settings = userSettingsRepository
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun createObject(
        repo: BlockRepository,
        getDefaultObjectType: GetDefaultObjectType,
        dispatchers: AppCoroutineDispatchers
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
    @PerScreen
    @Provides
    fun getSetObjectListIsArchived(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)

    @Provides
    @PerScreen
    @JvmStatic
    fun removeObjectFromWorkspace(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): RemoveObjectsFromWorkspace = RemoveObjectsFromWorkspace(repo, dispatchers)

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: AllContentViewModelFactory
        ): ViewModelProvider.Factory

    }
}

interface AllContentDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun userSettingsRepository(): UserSettingsRepository
    fun subEventChannel(): SubscriptionEventChannel
    fun logger(): Logger
    fun localeProvider(): LocaleProvider
    fun userPermissionProvider(): UserPermissionProvider
    fun searchObjects(): SearchObjects
    fun fieldsProvider(): FieldParser
    fun spaceViews(): SpaceViewSubscriptionContainer
    fun provideSpaceSyncAndP2PStatusProvider(): SpaceSyncAndP2PStatusProvider
}