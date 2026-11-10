package com.chronoio.chrono.di.feature.chats

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.core_utils.notifications.NotificationPermissionManager
import com.chronoio.chrono.core_utils.tools.FeatureToggles
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.chats.ChatEventChannel
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.domain.event.interactor.EventChannel
import com.chronoio.chrono.domain.invite.SpaceInviteLinkStore
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.notifications.NotificationBuilder
import com.chronoio.chrono.domain.notifications.ResetSpaceChatNotification
import com.chronoio.chrono.domain.notifications.SetChatNotificationMode
import com.chronoio.chrono.domain.`object`.GetObject
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.spaces.ClearLastOpenedSpace
import com.chronoio.chrono.domain.widgets.CreateWidget
import com.chronoio.chrono.domain.widgets.DeleteWidget
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.feature_chats.presentation.ChatViewModel
import com.chronoio.chrono.feature_chats.presentation.ChatViewModelFactory
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.presentation.common.PayloadDelegator
import com.chronoio.chrono.presentation.navigation.backstack.BackHistoryDelegate
import com.chronoio.chrono.presentation.navigation.backstack.NavigationBackStackInspector
import com.chronoio.chrono.presentation.util.CopyFileToCacheDirectory
import com.chronoio.chrono.presentation.util.defaultCopyFileToCacheDirectory
import com.chronoio.chrono.presentation.vault.ExitToVaultDelegate
import com.chronoio.chrono.presentation.widgets.PinObjectAsWidgetDelegate
import com.chronoio.chrono.ui.chats.ChatFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

@Component(
    dependencies = [ChatComponentDependencies::class],
    modules = [
        ChatContainerModule::class,
        ChatModule::class,
        ChatModule.Declarations::class
    ]
)
@PerScreen
interface ChatComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: ChatViewModel.Params.Default): Builder
        fun withDependencies(dependencies: ChatComponentDependencies): Builder
        fun build(): ChatComponent
    }
    fun inject(fragment: ChatFragment)
}

@Module
object ChatModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCopyFileToCache(
        context: Context
    ): CopyFileToCacheDirectory = defaultCopyFileToCacheDirectory(context)

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
    @PerScreen
    @Provides
    fun providePinObjectAsWidgetDelegate(
        spaceManager: SpaceManager,
        createWidget: CreateWidget,
        deleteWidget: DeleteWidget,
        showObject: GetObject,
        payloadDelegator: PayloadDelegator
    ) : PinObjectAsWidgetDelegate = PinObjectAsWidgetDelegate.Default(
        spaceManager = spaceManager,
        createWidget = createWidget,
        deleteWidget = deleteWidget,
        showObject = showObject,
        payloadDelegator = payloadDelegator
    )

    @JvmStatic
    @PerScreen
    @Provides
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

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: ChatViewModelFactory
        ): ViewModelProvider.Factory
    }
}

interface ChatComponentDependencies : ComponentDependencies {
    @Named(DEFAULT_APP_COROUTINE_SCOPE) fun scope(): CoroutineScope
    fun blockRepository(): BlockRepository
    fun authRepo(): AuthRepository
    fun appCoroutineDispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun userPermissionProvider(): UserPermissionProvider
    fun eventProxy(): EventProxy
    fun featureToggles(): FeatureToggles
    fun userSettings(): UserSettingsRepository
    fun chatEventChannel(): ChatEventChannel
    fun logger(): Logger
    fun members(): ActiveSpaceMemberSubscriptionContainer
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun context(): Context
    fun spaceManager(): SpaceManager
    fun notificationPermissionManager(): NotificationPermissionManager
    fun storelessSubscriptionContainer(): StorelessSubscriptionContainer
    fun notificationBuilder(): NotificationBuilder
    fun eventChannel(): EventChannel
    fun spaceLinkInfoStore(): SpaceInviteLinkStore
    fun setChatNotificationMode(): SetChatNotificationMode
    fun resetSpaceChatNotification(): ResetSpaceChatNotification
    fun payloadDelegator(): PayloadDelegator
    fun fieldParser(): FieldParser
    fun deepLinkResolver(): DeepLinkResolver
    fun navigationBackStackInspector(): NavigationBackStackInspector
}
