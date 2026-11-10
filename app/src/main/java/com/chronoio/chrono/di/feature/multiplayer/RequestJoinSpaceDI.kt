package com.chronoio.chrono.di.feature.multiplayer

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceInviteResolver
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.notifications.SystemNotificationService
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.other.DefaultSpaceInviteResolver
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.multiplayer.RequestJoinSpaceViewModel
import com.chronoio.chrono.ui.multiplayer.RequestJoinSpaceFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [RequestJoinSpaceDependencies::class],
    modules = [
        RequestJoinSpaceModule::class,
        RequestJoinSpaceModule.Declarations::class
    ]
)
@PerDialog
interface RequestJoinSpaceComponent {

    @Component.Builder
    interface Builder {
        fun withDependencies(dependencies: RequestJoinSpaceDependencies): Builder
        @BindsInstance
        fun withParams(params: RequestJoinSpaceViewModel.Params): Builder
        fun build(): RequestJoinSpaceComponent
    }

    fun inject(fragment: RequestJoinSpaceFragment)
}

@Module
object RequestJoinSpaceModule {
    @Module
    interface Declarations {
        @PerDialog
        @Binds
        fun bindViewModelFactory(factory: RequestJoinSpaceViewModel.Factory): ViewModelProvider.Factory
    }

    @PerDialog
    @Provides
    fun provideSpaceInviteResolver() : SpaceInviteResolver = DefaultSpaceInviteResolver
}

interface RequestJoinSpaceDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun auth(): AuthRepository
    fun settings(): UserSettingsRepository
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun spaceManager(): SpaceManager
    fun logger(): Logger
    fun analytics(): Analytics
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun notificator(): SystemNotificationService
    fun configStorage(): ConfigStorage
    fun spaceViewContainer(): SpaceViewSubscriptionContainer
}