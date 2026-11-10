package com.chronoio.chrono.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.tools.AppInfo
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.account.InterceptAccountStatus
import com.chronoio.chrono.domain.auth.interactor.AppShutdown
import com.chronoio.chrono.domain.auth.interactor.CheckAuthorizationStatus
import com.chronoio.chrono.domain.auth.interactor.HasAccount
import com.chronoio.chrono.domain.auth.interactor.Logout
import com.chronoio.chrono.domain.auth.interactor.ResumeAccount
import com.chronoio.chrono.domain.chats.ChatPreviewContainer
import com.chronoio.chrono.domain.chats.ChatsDetailsSubscriptionContainer
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.ObserveShowSpacesIntroduction
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.DebugRunProfiler
import com.chronoio.chrono.domain.deeplink.PendingIntentStore
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.launch.RemainingSpacesPreloader
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.domain.multiplayer.ParticipantSubscriptionContainer
import com.chronoio.chrono.domain.objects.CrossSpaceObjectTypesContainer
import com.chronoio.chrono.domain.multiplayer.SpaceInviteResolver
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.notifications.SystemNotificationService
import com.chronoio.chrono.domain.spaces.ResolveSpaceHomepage
import com.chronoio.chrono.domain.subscriptions.GlobalSubscriptionManager
import com.chronoio.chrono.domain.vault.SetSpacesIntroductionShown
import com.chronoio.chrono.domain.wallpaper.ObserveSpaceWallpaper
import com.chronoio.chrono.domain.workspace.DeepLinkToObjectDelegate
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.membership.provider.MembershipProvider
import com.chronoio.chrono.presentation.notifications.NotificationActionDelegate
import com.chronoio.chrono.presentation.notifications.NotificationsProvider
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope

class MainViewModelFactory @Inject constructor(
    private val resumeAccount: ResumeAccount,
    private val analytics: Analytics,
    private val interceptAccountStatus: InterceptAccountStatus,
    private val logout: Logout,
    private val checkAuthorizationStatus: CheckAuthorizationStatus,
    private val hasAccount: HasAccount,
    private val configStorage: ConfigStorage,
    private val localeProvider: LocaleProvider,
    private val notificationsProvider: NotificationsProvider,
    private val notificator: SystemNotificationService,
    private val notificationActionDelegate: NotificationActionDelegate,
    private val deepLinkToObjectDelegate: DeepLinkToObjectDelegate,
    private val awaitAccountStartManager: AwaitAccountStartManager,
    private val membershipProvider: MembershipProvider,
    private val globalSubscriptionManager: GlobalSubscriptionManager,
    private val spaceInviteResolver: SpaceInviteResolver,
    private val spaceManager: SpaceManager,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val pendingIntentStore: PendingIntentStore,
    private val observeSpaceWallpaper: ObserveSpaceWallpaper,
    private val urlBuilder: UrlBuilder,
    private val appShutdown: AppShutdown,
    private val scope: CoroutineScope,
    private val observeShowSpacesIntroduction: ObserveShowSpacesIntroduction,
    private val setSpacesIntroductionShown: SetSpacesIntroductionShown,
    private val appInfo: AppInfo,
    private val chatPreviewContainer: ChatPreviewContainer,
    private val chatsDetailsSubscriptionContainer: ChatsDetailsSubscriptionContainer,
    private val participantSubscriptionContainer: ParticipantSubscriptionContainer,
    private val crossSpaceObjectTypesContainer: CrossSpaceObjectTypesContainer,
    private val userSettingsRepository: UserSettingsRepository,
    private val resolveSpaceHomepage: ResolveSpaceHomepage,
    private val debugRunProfiler: DebugRunProfiler,
    private val preferredSpaceIdHolder: PreferredSpaceIdHolder,
    private val remainingSpacesPreloader: RemainingSpacesPreloader
    ) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T = MainViewModel(
        resumeAccount = resumeAccount,
        analytics = analytics,
        interceptAccountStatus = interceptAccountStatus,
        logout = logout,
        checkAuthorizationStatus = checkAuthorizationStatus,
        hasAccount = hasAccount,
        configStorage = configStorage,
        localeProvider = localeProvider,
        notificationsProvider = notificationsProvider,
        notificator = notificator,
        notificationActionDelegate = notificationActionDelegate,
        deepLinkToObjectDelegate = deepLinkToObjectDelegate,
        awaitAccountStartManager = awaitAccountStartManager,
        membershipProvider = membershipProvider,
        globalSubscriptionManager = globalSubscriptionManager,
        spaceInviteResolver = spaceInviteResolver,
        spaceManager = spaceManager,
        spaceViews = spaceViews,
        pendingIntentStore = pendingIntentStore,
        observeSpaceWallpaper = observeSpaceWallpaper,
        urlBuilder = urlBuilder,
        appShutdown = appShutdown,
        scope = scope,
        observeShowSpacesIntroduction = observeShowSpacesIntroduction,
        setSpacesIntroductionShown = setSpacesIntroductionShown,
        appInfo = appInfo,
        chatPreviewContainer = chatPreviewContainer,
        chatsDetailsSubscriptionContainer = chatsDetailsSubscriptionContainer,
        participantSubscriptionContainer = participantSubscriptionContainer,
        crossSpaceObjectTypesContainer = crossSpaceObjectTypesContainer,
        userSettingsRepository = userSettingsRepository,
        resolveSpaceHomepage = resolveSpaceHomepage,
        debugRunProfiler = debugRunProfiler,
        preferredSpaceIdHolder = preferredSpaceIdHolder,
        remainingSpacesPreloader = remainingSpacesPreloader
    ) as T
}