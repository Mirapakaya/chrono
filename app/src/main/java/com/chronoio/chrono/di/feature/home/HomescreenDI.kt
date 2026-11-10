package com.chronoio.chrono.di.feature.home

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.core_utils.notifications.NotificationPermissionManager
import com.chronoio.chrono.core_utils.tools.FeatureToggles
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.bin.EmptyBin
import com.chronoio.chrono.domain.block.interactor.Move
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.chats.ChatEventChannel
import com.chronoio.chrono.domain.chats.ChatPreviewContainer
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.device.FileSharer
import com.chronoio.chrono.domain.event.interactor.EventChannel
import com.chronoio.chrono.domain.event.interactor.InterceptEvents
import com.chronoio.chrono.domain.favorites.PersonalFavoritesRepository
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.media.UploadFile
import com.chronoio.chrono.domain.misc.AppActionManager
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import com.chronoio.chrono.domain.notifications.SetSpaceNotificationMode
import com.chronoio.chrono.domain.multiplayer.ParticipantSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.SpaceInviteResolver
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.GetObject
import com.chronoio.chrono.domain.`object`.OpenObject
import com.chronoio.chrono.domain.`object`.SetObjectDetails
import com.chronoio.chrono.domain.objects.ObjectStore
import com.chronoio.chrono.domain.objects.ObjectWatcher
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.page.CloseObject
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.HasInstanceOfObjectTypeSubscriptionContainer
import com.chronoio.chrono.domain.search.SubscriptionEventChannel
import com.chronoio.chrono.domain.spaces.ClearLastOpenedSpace
import com.chronoio.chrono.domain.spaces.SetHomepage
import com.chronoio.chrono.domain.templates.GetTemplates
import com.chronoio.chrono.domain.workspace.DeepLinkToObjectDelegate
import com.chronoio.chrono.domain.workspace.NotificationsChannel
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.other.DefaultSpaceInviteResolver
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.common.PayloadDelegator
import com.chronoio.chrono.presentation.editor.cover.CoverImageHashProvider
import com.chronoio.chrono.presentation.home.HomeScreenViewModel
import com.chronoio.chrono.presentation.home.HomeScreenVmParams
import com.chronoio.chrono.presentation.home.Unsubscriber
import com.chronoio.chrono.presentation.spaces.SpaceGradientProvider
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.vault.ExitToVaultDelegate
import com.chronoio.chrono.presentation.widgets.DefaultObjectViewReducer
import com.chronoio.chrono.presentation.widgets.WidgetActiveViewStateHolder
import com.chronoio.chrono.presentation.widgets.WidgetDispatchEvent
import com.chronoio.chrono.presentation.widgets.WidgetSessionStateHolder
import com.chronoio.chrono.providers.DefaultCoverImageHashProvider
import com.chronoio.chrono.ui.home.WidgetOverlayFragment
import com.chronoio.chrono.ui.home.WidgetsScreenFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Component(
    dependencies = [HomeScreenDependencies::class],
    modules = [
        HomeScreenModule::class,
        HomeScreenModule.Declarations::class
    ]
)
@PerScreen
interface HomeScreenComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: HomeScreenVmParams,
            dependencies: HomeScreenDependencies
        ): HomeScreenComponent
    }

    fun inject(fragment: WidgetsScreenFragment)
    fun inject(fragment: WidgetOverlayFragment)
}

@Module
object HomeScreenModule {

    @Provides
    fun provideUnqualifiedScope(
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): CoroutineScope = scope

    @JvmStatic
    @Provides
    @PerScreen
    fun openObject(
        repo: BlockRepository,
        settings: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): OpenObject = OpenObject(
        repo = repo,
        dispatchers = dispatchers,
        settings = settings
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun closeObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CloseObject = CloseObject(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun setObjectDetails(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun move(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ) : Move = Move(
        repo = repo,
        appCoroutineDispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun emptyBin(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        spaceManager: SpaceManager
    ) : EmptyBin = EmptyBin(
        repo = repo,
        dispatchers = dispatchers,
        spaceManager = spaceManager
    )

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
    fun createObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        getDefaultEditorType: GetDefaultObjectType,
        spaceManager: SpaceManager
    ): CreateObject = CreateObject(
        repo = repo,
        dispatchers = dispatchers,
        getDefaultObjectType = getDefaultEditorType
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun getDefaultPageType(
        userSettingsRepository: UserSettingsRepository,
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ) : GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = userSettingsRepository,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun getTemplates(
        repo: BlockRepository,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers,
    ) : GetTemplates = GetTemplates(
        repo = repo,
        spaceManager = spaceManager,
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
    @Provides
    @PerScreen
    fun gradientProvider(): SpaceGradientProvider = SpaceGradientProvider.Default

    @JvmStatic
    @Provides
    @PerScreen
    fun coverHashProvider() : CoverImageHashProvider = DefaultCoverImageHashProvider()

    @JvmStatic
    @PerScreen
    @Provides
    fun provideSpaceInviteResolver(): SpaceInviteResolver = DefaultSpaceInviteResolver

    @JvmStatic
    @Provides
    @PerScreen
    fun setHomepage(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetHomepage = SetHomepage(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideExitToVaultDelegate(
        spaceManager: SpaceManager,
        clearLastOpenedSpace: ClearLastOpenedSpace
    ) : ExitToVaultDelegate = ExitToVaultDelegate.Default(
        spaceManager = spaceManager,
        clearLastOpenedSpace = clearLastOpenedSpace
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUploadFile(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): UploadFile = UploadFile(repo = repo, dispatchers = dispatchers)

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun factory(factory: HomeScreenViewModel.Factory): ViewModelProvider.Factory

        @PerScreen
        @Binds
        fun container(container: StorelessSubscriptionContainer.Impl): StorelessSubscriptionContainer

        @PerScreen
        @Binds
        fun holder(holder: WidgetActiveViewStateHolder.Impl): WidgetActiveViewStateHolder

        @PerScreen
        @Binds
        fun unsubscriber(impl: Unsubscriber.Impl) : Unsubscriber

        @PerScreen
        @Binds
        fun widgetSessionStateHolder(
            holder: WidgetSessionStateHolder.Impl
        ): WidgetSessionStateHolder

        @PerScreen
        @Binds
        fun objectWatcherReducer(
            default: DefaultObjectViewReducer
        ): ObjectWatcher.Reducer

        @PerScreen
        @Binds
        fun deepLinkToObjectDelegate(
            default: DeepLinkToObjectDelegate.Default
        ) : DeepLinkToObjectDelegate
    }
}

interface HomeScreenDependencies : ComponentDependencies {
    fun dispatcherWidgets(): Dispatcher<WidgetDispatchEvent>
    fun dispatcherPayload(): Dispatcher<Payload>
    fun blockRepo(): BlockRepository
    fun personalFavoritesRepo(): PersonalFavoritesRepository
    fun authRepo(): AuthRepository
    fun userRepo(): UserSettingsRepository
    fun config(): ConfigStorage
    fun urlBuilder(): UrlBuilder
    fun objectStore(): ObjectStore
    fun subscriptionEventChannel(): SubscriptionEventChannel
    fun analytics(): Analytics
    fun eventChannel(): EventChannel
    fun dispatchers(): AppCoroutineDispatchers
    fun appActionManager(): AppActionManager
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun hasInstanceOfObjectTypeSubscriptionContainer(): HasInstanceOfObjectTypeSubscriptionContainer
    fun dateProvider(): DateProvider
    fun logger(): Logger
    fun spaceManager(): SpaceManager
    fun userPermissionProvider(): UserPermissionProvider
    fun notificationChannel(): NotificationsChannel
    fun activeSpaceMembers() : ActiveSpaceMemberSubscriptionContainer
    fun analyticSpaceHelperDelegate(): AnalyticSpaceHelperDelegate
    fun storeOfRelations(): StoreOfRelations
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun participantSubscriptionContainer(): ParticipantSubscriptionContainer
    fun featureToggles(): FeatureToggles
    fun payloadDelegator(): PayloadDelegator
    fun fieldParser(): FieldParser
    fun notificationPermissionManager(): NotificationPermissionManager
    fun provideChatEventChannel(): ChatEventChannel
    fun provideChatPreviewContainer(): ChatPreviewContainer
    fun clipboard(): com.chronoio.chrono.domain.clipboard.Clipboard
    @Named(DEFAULT_APP_COROUTINE_SCOPE) fun scope(): CoroutineScope
    fun stringResProvider() : StringResourceProvider
    fun setSpaceNotificationMode(): SetSpaceNotificationMode
    fun fileSharer(): FileSharer
}
