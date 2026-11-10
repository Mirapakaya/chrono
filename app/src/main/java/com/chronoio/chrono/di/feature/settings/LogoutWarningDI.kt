package com.chronoio.chrono.di.feature.settings

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.interactor.Logout
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.launch.RemainingSpacesPreloader
import com.chronoio.chrono.domain.misc.AppActionManager
import com.chronoio.chrono.domain.subscriptions.GlobalSubscriptionManager
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.ui.settings.LogoutWarningFragment
import com.chronoio.chrono.ui_settings.account.LogoutWarningViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [LogoutWarningModule::class])
@PerScreen
interface LogoutWarningSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: LogoutWarningModule): Builder
        fun build(): LogoutWarningSubComponent
    }

    fun inject(fragment: LogoutWarningFragment)
}

@Module
object LogoutWarningModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideViewModelFactory(
        logout: Logout,
        analytics: Analytics,
        appActionManager: AppActionManager,
        globalSubscriptionManager: GlobalSubscriptionManager
    ): LogoutWarningViewModel.Factory = LogoutWarningViewModel.Factory(
        logout = logout,
        analytics = analytics,
        appActionManager = appActionManager,
        globalSubscriptionManager = globalSubscriptionManager
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
        config = provider,
        user = user,
        dispatchers = dispatchers,
        spaceManager = spaceManager,
        awaitAccountStartManager = awaitAccountStartManager,
        remainingSpacesPreloader = remainingSpacesPreloader,
        preferredSpaceIdHolder = preferredSpaceIdHolder
    )
}