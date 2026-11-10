package com.chronoio.chrono.di.feature.onboarding.login

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.CrashReporter
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.data.auth.event.EventProcessMigrationDateChannel
import com.chronoio.chrono.data.auth.event.EventProcessMigrationRemoteChannel
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.DebugAccountSelectTrace
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.device.PathProvider
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.launch.RemainingSpacesPreloader
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.platform.InitialParamsProvider
import com.chronoio.chrono.domain.spaces.SpaceDeletedStatusWatcher
import com.chronoio.chrono.domain.subscriptions.GlobalSubscriptionManager
import com.chronoio.chrono.domain.workspace.EventProcessMigrationChannel
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.middleware.interactor.EventProcessMigrationMiddlewareChannel
import com.chronoio.chrono.presentation.auth.account.MigrationHelperDelegate
import com.chronoio.chrono.presentation.onboarding.login.OnboardingMnemonicLoginViewModel
import com.chronoio.chrono.presentation.util.downloader.UriFileProvider
import com.chronoio.chrono.providers.DefaultUriFileProvider
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [OnboardingMnemonicLoginDependencies::class],
    modules = [
        OnboardingMnemonicLoginModule::class,
        OnboardingMnemonicLoginModule.Declarations::class
    ]
)
@PerScreen
interface OnboardingMnemonicLoginComponent {
    @Component.Factory
    interface Builder {
        fun create(
            dependencies: OnboardingMnemonicLoginDependencies
        ): OnboardingMnemonicLoginComponent
    }

    fun getViewModel(): OnboardingMnemonicLoginViewModel
}

@Module
object OnboardingMnemonicLoginModule {

    @Module
    interface Declarations {

        @Binds
        @PerScreen
        fun bindUriFileProvider(
            defaultProvider: DefaultUriFileProvider
        ): UriFileProvider

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

        @Binds
        @PerScreen
        fun bindViewModelFactory(
            factory: OnboardingMnemonicLoginViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface OnboardingMnemonicLoginDependencies : ComponentDependencies {
    fun authRepository(): AuthRepository
    fun analytics(): Analytics
    fun pathProvider(): PathProvider
    fun crashReporter(): CrashReporter
    fun configStorage(): ConfigStorage
    fun metricsProvider(): InitialParamsProvider
    fun spaceStatusWatcher(): SpaceDeletedStatusWatcher
    fun localeProvider(): LocaleProvider
    fun awaitAccountStartManager(): AwaitAccountStartManager
    fun userPermissionProvider() : UserPermissionProvider
    fun provideAppCoroutineDispatchers(): AppCoroutineDispatchers
    fun provideBlockRepository(): BlockRepository
    fun provideContext(): Context
    fun userSettings(): UserSettingsRepository
    fun spaceManager(): SpaceManager
    fun globalSubscriptionManager(): GlobalSubscriptionManager
    fun debugAccountSelectTrace(): DebugAccountSelectTrace
    fun logger(): Logger
    fun eventProxy(): EventProxy
    fun remainingSpacesPreloader(): RemainingSpacesPreloader
    fun preferredSpaceIdHolder(): PreferredSpaceIdHolder
}