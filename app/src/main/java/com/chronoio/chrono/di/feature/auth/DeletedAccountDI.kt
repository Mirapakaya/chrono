package com.chronoio.chrono.di.feature.auth

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.account.DateHelper
import com.chronoio.chrono.domain.account.RestoreAccount
import com.chronoio.chrono.domain.auth.interactor.Logout
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.launch.RemainingSpacesPreloader
import com.chronoio.chrono.domain.misc.AppActionManager
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.subscriptions.GlobalSubscriptionManager
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.ext.DefaultDateHelper
import com.chronoio.chrono.presentation.auth.account.DeletedAccountViewModel
import com.chronoio.chrono.ui.auth.account.DeletedAccountFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [DeletedAccountDependencies::class],
    modules = [
        DeletedAccountModule::class,
        DeletedAccountModule.Declarations::class
    ]
)
@PerScreen
interface DeletedAccountComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: DeletedAccountDependencies): DeletedAccountComponent
    }

    fun inject(fragment: DeletedAccountFragment)
}

@Module
object DeletedAccountModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRestoreAccount(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): RestoreAccount = RestoreAccount(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideLogoutUseCase(
        repo: AuthRepository,
        provider: ConfigStorage,
        dispatchers: AppCoroutineDispatchers,
        user: UserSettingsRepository,
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
    fun provideDateHelper(): DateHelper = DefaultDateHelper()

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: DeletedAccountViewModel.Factory
        ): ViewModelProvider.Factory

    }
}

interface DeletedAccountDependencies : ComponentDependencies {
    fun analytics(): Analytics
    fun appActionManager(): AppActionManager
    fun dispatchers(): AppCoroutineDispatchers
    fun configStorage(): ConfigStorage
    fun authRepository(): AuthRepository
    fun userSettingsRepository(): UserSettingsRepository
    fun spaceManager(): SpaceManager
    fun awaitAccountStartManager(): AwaitAccountStartManager
    fun userPermissionProvider(): UserPermissionProvider
    fun globalSubscriptionManager(): GlobalSubscriptionManager
    fun remainingSpacesPreloader(): RemainingSpacesPreloader
    fun preferredSpaceIdHolder(): PreferredSpaceIdHolder
}