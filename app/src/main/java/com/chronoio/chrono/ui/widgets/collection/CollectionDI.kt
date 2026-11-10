package com.chronoio.chrono.ui.widgets.collection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.interactor.Move
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.dashboard.interactor.SetObjectListIsFavorite
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.event.interactor.EventChannel
import com.chronoio.chrono.domain.event.interactor.InterceptEvents
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.misc.DateTypeNameProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.OpenObject
import com.chronoio.chrono.domain.objects.DeleteObjects
import com.chronoio.chrono.domain.objects.ObjectStore
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.ObjectSearchSubscriptionContainer
import com.chronoio.chrono.domain.search.SubscriptionEventChannel
import com.chronoio.chrono.domain.templates.GetTemplates
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.widgets.WidgetDispatchEvent
import com.chronoio.chrono.presentation.widgets.collection.ResourceProvider
import com.chronoio.chrono.presentation.widgets.collection.CollectionViewModel
import com.chronoio.chrono.ui.settings.RemoteFilesManageFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Component(
    dependencies = [CollectionDependencies::class],
    modules = [CollectionModule::class]
)
@PerScreen
interface CollectionComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: CollectionViewModel.VmParams,
            dependencies: CollectionDependencies
        ): CollectionComponent
    }

    fun inject(fragment: CollectionFragment)
    fun inject(fragment: RemoteFilesManageFragment)

}

@Module(includes = [CollectionModule.Declarations::class])
object CollectionModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun openObject(
        repo: BlockRepository,
        settingsRepository: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): OpenObject = OpenObject(
        repo = repo,
        dispatchers = dispatchers,
        settings = settingsRepository
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideMoveUseCase(
        repo: BlockRepository,
        appCoroutineDispatchers: AppCoroutineDispatchers
    ): Move = Move(repo, appCoroutineDispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun objectSearchSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        store: ObjectStore,
        dispatchers: AppCoroutineDispatchers
    ): ObjectSearchSubscriptionContainer = ObjectSearchSubscriptionContainer(
        repo = repo,
        channel = channel,
        store = store,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun interceptEvents(channel: EventChannel): InterceptEvents = InterceptEvents(
        context = Dispatchers.IO,
        channel = channel
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun getSetObjectListIsArchived(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun getSetObjectListIsFavorite(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsFavorite = SetObjectListIsFavorite(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun getDeleteObjects(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteObjects = DeleteObjects(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun getCreateObject(
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

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun factory(factory: CollectionViewModel.Factory): ViewModelProvider.Factory

        @PerScreen
        @Binds
        fun bindContainer(container: StorelessSubscriptionContainer.Impl): StorelessSubscriptionContainer
    }
}

interface CollectionDependencies : ComponentDependencies {
    fun dispatcherWidgets(): Dispatcher<WidgetDispatchEvent>
    fun dispatcherPayload(): Dispatcher<Payload>
    fun context(): Context
    fun blockRepo(): BlockRepository
    fun authRepo(): AuthRepository
    fun config(): ConfigStorage
    fun urlBuilder(): UrlBuilder
    fun objectStore(): ObjectStore
    fun subscriptionEventChannel(): SubscriptionEventChannel
    fun analytics(): Analytics
    fun eventChannel(): EventChannel
    fun userSettingsRepository(): UserSettingsRepository
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun dispatchers(): AppCoroutineDispatchers
    fun spaceManager(): SpaceManager
    fun logger(): Logger
    fun dateProvider(): DateProvider
    fun dateTypeNameProvider(): DateTypeNameProvider
    fun analyticsHelperDelegate(): AnalyticSpaceHelperDelegate
    fun provideUserPermissionProvider(): UserPermissionProvider
    fun fieldParser(): FieldParser
    fun resourceProvider(): ResourceProvider
    fun spaceViews(): SpaceViewSubscriptionContainer
}