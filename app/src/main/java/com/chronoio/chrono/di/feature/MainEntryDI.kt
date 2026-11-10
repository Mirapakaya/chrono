package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import com.chronoio.chrono.domain.account.AccountStatusChannel
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.account.InterceptAccountStatus
import com.chronoio.chrono.domain.auth.interactor.AppShutdown
import com.chronoio.chrono.domain.auth.interactor.CheckAuthorizationStatus
import com.chronoio.chrono.domain.auth.interactor.HasAccount
import com.chronoio.chrono.domain.auth.interactor.Logout
import com.chronoio.chrono.domain.auth.interactor.ResumeAccount
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.chats.ChatPreviewContainer
import com.chronoio.chrono.domain.chats.ChatsDetailsSubscriptionContainer
import com.chronoio.chrono.domain.launch.RemainingSpacesPreloader
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.ObserveShowSpacesIntroduction
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.DebugRunProfiler
import com.chronoio.chrono.domain.deeplink.PendingIntentStore
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.device.PathProvider
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.domain.multiplayer.ParticipantSubscriptionContainer
import com.chronoio.chrono.domain.objects.CrossSpaceObjectTypesContainer
import com.chronoio.chrono.domain.multiplayer.SpaceInviteResolver
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.notifications.SystemNotificationService
import com.chronoio.chrono.domain.platform.InitialParamsProvider
import com.chronoio.chrono.domain.spaces.ResolveSpaceHomepage
import com.chronoio.chrono.domain.subscriptions.GlobalSubscriptionManager
import com.chronoio.chrono.domain.theme.GetTheme
import com.chronoio.chrono.domain.vault.SetSpacesIntroductionShown
import com.chronoio.chrono.domain.wallpaper.ObserveSpaceWallpaper
import com.chronoio.chrono.domain.workspace.DeepLinkToObjectDelegate
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.other.DefaultSpaceInviteResolver
import com.chronoio.chrono.presentation.main.MainViewModelFactory
import com.chronoio.chrono.presentation.membership.provider.MembershipProvider
import com.chronoio.chrono.presentation.notifications.NotificationActionDelegate
import com.chronoio.chrono.presentation.notifications.NotificationsProvider
import com.chronoio.chrono.ui.main.MainActivity
import com.chronoio.chrono.ui_settings.appearance.ThemeApplicator
import com.chronoio.chrono.ui_settings.appearance.ThemeApplicatorImpl
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

@Subcomponent(
    modules = [MainEntryModule::class]
)
@PerScreen
interface MainEntrySubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MainEntrySubComponent
        fun module(module: MainEntryModule): Builder
    }

    fun inject(activity: MainActivity)
}

@Module
object MainEntryModule {

    @Provides
    fun provideUnqualifiedScope(
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): CoroutineScope = scope

    @JvmStatic
    @PerScreen
    @Provides
    fun provideMainViewModelFactory(
        resumeAccount: ResumeAccount,
        analytics: Analytics,
        interceptAccountStatus: InterceptAccountStatus,
        logout: Logout,
        checkAuthorizationStatus: CheckAuthorizationStatus,
        hasAccount: HasAccount,
        configStorage: ConfigStorage,
        localeProvider: LocaleProvider,
        notificationsProvider: NotificationsProvider,
        notificator: SystemNotificationService,
        notificationActionDelegate: NotificationActionDelegate,
        deepLinkToObjectDelegate: DeepLinkToObjectDelegate,
        awaitAccountStartManager: AwaitAccountStartManager,
        membershipProvider: MembershipProvider,
        globalSubscriptionManager: GlobalSubscriptionManager,
        spaceInviteResolver: SpaceInviteResolver,
        spaceManager: SpaceManager,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
        pendingIntentStore: PendingIntentStore,
        observeSpaceWallpaper: ObserveSpaceWallpaper,
        urlBuilder: UrlBuilder,
        appShutdown: AppShutdown,
        scope: CoroutineScope,
        observeShowSpacesIntroduction: ObserveShowSpacesIntroduction,
        setSpacesIntroductionShown: SetSpacesIntroductionShown,
        appInfo: com.chronoio.chrono.core_utils.tools.AppInfo,
        chatPreviewContainer: ChatPreviewContainer,
        chatsDetailsSubscriptionContainer: ChatsDetailsSubscriptionContainer,
        participantSubscriptionContainer: ParticipantSubscriptionContainer,
        crossSpaceObjectTypesContainer: CrossSpaceObjectTypesContainer,
        userSettingsRepository: UserSettingsRepository,
        resolveSpaceHomepage: ResolveSpaceHomepage,
        debugRunProfiler: DebugRunProfiler,
        preferredSpaceIdHolder: PreferredSpaceIdHolder,
        remainingSpacesPreloader: RemainingSpacesPreloader
    ): MainViewModelFactory = MainViewModelFactory(
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
        spaceViews = spaceViewSubscriptionContainer,
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
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideResumeAccountUseCase(
        authRepository: AuthRepository,
        pathProvider: PathProvider,
        configStorage: ConfigStorage,
        initialParamsProvider: InitialParamsProvider,
        awaitAccountStartManager: AwaitAccountStartManager,
        spaceManager: SpaceManager,
        settingsRepository: UserSettingsRepository
    ): ResumeAccount = ResumeAccount(
        repository = authRepository,
        pathProvider = pathProvider,
        configStorage = configStorage,
        initialParamsProvider = initialParamsProvider,
        awaitAccountStartManager = awaitAccountStartManager,
        spaceManager = spaceManager,
        settings = settingsRepository
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideSetupThemeUseCase(
        repo: UserSettingsRepository
    ): GetTheme = GetTheme(repo)

    @JvmStatic
    @PerScreen
    @Provides
    fun provideThemeApplicator(): ThemeApplicator = ThemeApplicatorImpl()

    @JvmStatic
    @PerScreen
    @Provides
    fun provideInterceptAccountStatus(
        channel: AccountStatusChannel,
        dispatchers: AppCoroutineDispatchers
    ): InterceptAccountStatus = InterceptAccountStatus(
        channel = channel,
        dispatchers = dispatchers
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideLogoutUseCase(
        repo: AuthRepository,
        provider: ConfigStorage,
        user: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers,
        spaceManager: SpaceManager,
        awaitAccountStartManager: AwaitAccountStartManager,
        remainingSpacesPreloader: RemainingSpacesPreloader,
        preferredSpaceIdHolder: PreferredSpaceIdHolder
    ): Logout = Logout(
        repo = repo,
        user = user,
        config = provider,
        dispatchers = dispatchers,
        spaceManager = spaceManager,
        awaitAccountStartManager = awaitAccountStartManager,
        remainingSpacesPreloader = remainingSpacesPreloader,
        preferredSpaceIdHolder = preferredSpaceIdHolder
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideCheckAuthStatus(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): CheckAuthorizationStatus = CheckAuthorizationStatus(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun provideHasAccount(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): HasAccount = HasAccount(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun provideNotificationActionDelegate(
        default: NotificationActionDelegate.Default
    ) : NotificationActionDelegate = default

    @JvmStatic
    @PerScreen
    @Provides
    fun provideDeepLinkToObjectDelegate(
        default: DeepLinkToObjectDelegate.Default
    ): DeepLinkToObjectDelegate = default

    @JvmStatic
    @PerScreen
    @Provides
    fun provideSpaceInviteResolver(): SpaceInviteResolver = DefaultSpaceInviteResolver

    @JvmStatic
    @PerScreen
    @Provides
    fun provideDebugRunProfiler(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): DebugRunProfiler = DebugRunProfiler(repo, dispatchers)
}