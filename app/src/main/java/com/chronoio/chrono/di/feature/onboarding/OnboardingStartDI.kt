package com.chronoio.chrono.di.feature.onboarding

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.CrashReporter
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.di.feature.onboarding.signup.SoulCreationScreenScope
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.interactor.CreateAccount
import com.chronoio.chrono.domain.auth.interactor.SetupWallet
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.deeplink.PendingIntentStore
import com.chronoio.chrono.domain.device.PathProvider
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.ImportGetStartedUseCase
import com.chronoio.chrono.domain.platform.InitialParamsProvider
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.RelationsSubscriptionManager
import com.chronoio.chrono.domain.spaces.SpaceDeletedStatusWatcher
import com.chronoio.chrono.domain.subscriptions.GlobalSubscriptionManager
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.onboarding.OnboardingStartViewModel
import com.chronoio.chrono.presentation.spaces.SpaceGradientProvider
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(
    dependencies = [OnboardingStartDependencies::class],
    modules = [
        OnboardingStartModule::class,
        OnboardingStartModule.Declarations::class
    ]
)
@AuthScreenScope
interface OnboardingStartComponent {
    @Component.Factory
    interface Builder {
        fun create(dependencies: OnboardingStartDependencies): OnboardingStartComponent
    }

    fun getViewModel(): OnboardingStartViewModel
}

@Module
object OnboardingStartModule {

    @JvmStatic
    @Provides
    @AuthScreenScope
    fun gradientProvider(): SpaceGradientProvider = SpaceGradientProvider.Default

    @JvmStatic
    @Provides
    @AuthScreenScope
    fun provideCreateAccountUseCase(
        authRepository: AuthRepository,
        configStorage: ConfigStorage,
        initialParamsProvider: InitialParamsProvider,
        awaitAccountStartManager: AwaitAccountStartManager,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers
    ): CreateAccount = CreateAccount(
        repository = authRepository,
        configStorage = configStorage,
        initialParamsProvider = initialParamsProvider,
        dispatcher = dispatchers,
        awaitAccountStartManager = awaitAccountStartManager,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @AuthScreenScope
    fun provideSetupWalletUseCase(
        authRepository: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetupWallet = SetupWallet(
        repository = authRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @AuthScreenScope
    fun provideSetupSkipUseCase(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ) = ImportGetStartedUseCase(
        repo = repository,
        dispatchers = dispatchers
    )

    @Module
    interface Declarations {
        @Binds
        @AuthScreenScope
        fun bindViewModelFactory(factory: OnboardingStartViewModel.Factory): ViewModelProvider.Factory
    }
}

interface OnboardingStartDependencies : ComponentDependencies {
    fun analytics(): Analytics
    fun blockRepository(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun configStorage(): ConfigStorage
    fun authRepository(): AuthRepository
    fun userSettings(): UserSettingsRepository
    fun relationsSubscriptionManager(): RelationsSubscriptionManager
    fun pathProvider(): PathProvider
    fun metricsProvider(): InitialParamsProvider
    fun crashReporter(): CrashReporter
    fun spaceManager(): SpaceManager
    fun spaceStatusWatcher(): SpaceDeletedStatusWatcher
    fun localeProvider(): LocaleProvider
    fun userPermissionProvider(): UserPermissionProvider
    fun awaitAccountStartManager(): AwaitAccountStartManager
    fun globalSubscriptionManager(): GlobalSubscriptionManager
    fun stringResourceProvider(): StringResourceProvider
    fun providePendingIntentStore(): PendingIntentStore
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthScreenScope