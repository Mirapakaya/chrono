package com.chronoio.chrono.di.feature.settings

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.core_utils.notifications.NotificationPermissionManager
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.account.DeleteAccount
import com.chronoio.chrono.domain.auth.interactor.Logout
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.launch.RemainingSpacesPreloader
import com.chronoio.chrono.domain.debugging.DebugSpace
import com.chronoio.chrono.domain.icon.RemoveObjectIcon
import com.chronoio.chrono.domain.icon.SetDocumentImageIcon
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.GenerateOneToOneChatLink
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.networkmode.GetNetworkMode
import com.chronoio.chrono.domain.`object`.SetObjectDetails
import com.chronoio.chrono.domain.search.ProfileSubscriptionManager
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.membership.provider.MembershipProvider
import com.chronoio.chrono.presentation.spaces.SpaceGradientProvider
import com.chronoio.chrono.ui.settings.ProfileSettingsFragment
import com.chronoio.chrono.ui_settings.account.ProfileSettingsViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [ProfileModule::class])
@PerScreen
interface ProfileSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: ProfileModule): Builder
        fun build(): ProfileSubComponent
    }

    fun inject(fragment: ProfileSettingsFragment)
}

@Module(includes = [ProfileModule.Bindings::class])
object ProfileModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideViewModelFactory(
        analytics: Analytics,
        storelessSubscriptionContainer: StorelessSubscriptionContainer,
        setObjectDetails: SetObjectDetails,
        configStorage: ConfigStorage,
        urlBuilder: UrlBuilder,
        setDocumentImageIcon: SetDocumentImageIcon,
        membershipProvider: MembershipProvider,
        getNetworkMode: GetNetworkMode,
        profileSubscriptionManager: ProfileSubscriptionManager,
        removeObjectIcon: RemoveObjectIcon,
        notificationPermissionManager: NotificationPermissionManager,
        userPermissionProvider: UserPermissionProvider,
        generateOneToOneChatLink: GenerateOneToOneChatLink,
        userSettingsRepository: UserSettingsRepository
    ): ProfileSettingsViewModel.Factory = ProfileSettingsViewModel.Factory(
        analytics = analytics,
        container = storelessSubscriptionContainer,
        setObjectDetails = setObjectDetails,
        configStorage = configStorage,
        urlBuilder = urlBuilder,
        setDocumentImageIcon = setDocumentImageIcon,
        membershipProvider = membershipProvider,
        getNetworkMode = getNetworkMode,
        profileSubscriptionManager = profileSubscriptionManager,
        removeObjectIcon = removeObjectIcon,
        notificationPermissionManager = notificationPermissionManager,
        userPermissionProvider = userPermissionProvider,
        generateOneToOneChatLink = generateOneToOneChatLink,
        userSettingsRepository = userSettingsRepository
    )

    @Provides
    @PerScreen
    fun provideSpaceGradientProvider(): SpaceGradientProvider = SpaceGradientProvider.Default

    @Provides
    @PerScreen
    fun provideDebugSync(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        spaceManager: SpaceManager
    ): DebugSpace = DebugSpace(
        repo = repo,
        dispatchers = dispatchers,
        spaceManager = spaceManager
    )

    @Provides
    @PerScreen
    fun provideRemoveObjectIcon(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): RemoveObjectIcon = RemoveObjectIcon(repo, dispatchers)

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
        config = provider,
        user = user,
        dispatchers = dispatchers,
        spaceManager = spaceManager,
        awaitAccountStartManager = awaitAccountStartManager,
        remainingSpacesPreloader = remainingSpacesPreloader,
        preferredSpaceIdHolder = preferredSpaceIdHolder
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun deleteAccount(repo: AuthRepository): DeleteAccount = DeleteAccount(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetObjectDetails(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(
        repo,
        dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDocumentImageIcon(
        repo: BlockRepository
    ): SetDocumentImageIcon = SetDocumentImageIcon(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGenerateOneToOneChatLink(
        dispatchers: AppCoroutineDispatchers
    ): GenerateOneToOneChatLink = GenerateOneToOneChatLink(
        dispatchers = dispatchers
    )

    @Module
    interface Bindings {
        // Add bindings if needed
    }
}