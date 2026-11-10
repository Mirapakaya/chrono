package com.chronoio.chrono.di.feature.onboarding.signup

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.CrashReporter
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.deeplink.PendingIntentStore
import com.chronoio.chrono.domain.device.PathProvider
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.payments.SetMembershipEmail
import com.chronoio.chrono.domain.platform.InitialParamsProvider
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.ProfileSubscriptionManager
import com.chronoio.chrono.domain.search.RelationsSubscriptionManager
import com.chronoio.chrono.domain.spaces.SpaceDeletedStatusWatcher
import com.chronoio.chrono.domain.subscriptions.GlobalSubscriptionManager
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.onboarding.signup.OnboardingSetProfileNameViewModel
import com.chronoio.chrono.presentation.onboarding.signup.OnboardingEmailAndSelectionViewModel
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(
    dependencies = [OnboardingSoulCreationDependencies::class],
    modules = [
        OnboardingSoulCreationModule::class,
        OnboardingSoulCreationModule.Declarations::class
    ]
)
@SoulCreationScreenScope
interface OnboardingSoulCreationComponent {

    @Component.Factory
    interface Builder {
        fun create(dependencies: OnboardingSoulCreationDependencies): OnboardingSoulCreationComponent
    }

    fun getViewModel(): OnboardingSetProfileNameViewModel
    fun getEmailAndSelectionViewModel(): OnboardingEmailAndSelectionViewModel
}

@Module
object OnboardingSoulCreationModule {

    @JvmStatic
    @Provides
    @SoulCreationScreenScope
    fun provideSetMembershipEmail(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetMembershipEmail = SetMembershipEmail(repo = repo, dispatchers = dispatchers)

    @Module
    interface Declarations {
        @Binds
        @SoulCreationScreenScope
        fun bindViewModelFactory(factory: OnboardingSetProfileNameViewModel.Factory): ViewModelProvider.Factory
        
        @Binds
        @SoulCreationScreenScope
        fun bindEmailAndSelectionViewModelFactory(factory: OnboardingEmailAndSelectionViewModel.Factory): ViewModelProvider.Factory
    }
}

interface OnboardingSoulCreationDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun configStorage(): ConfigStorage
    fun analytics(): Analytics
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
    fun provideProfileContainer() :ProfileSubscriptionManager
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SoulCreationScreenScope