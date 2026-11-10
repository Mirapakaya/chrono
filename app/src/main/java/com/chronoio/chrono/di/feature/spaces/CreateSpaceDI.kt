package com.chronoio.chrono.di.feature.spaces

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.search.ProfileSubscriptionManager
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.spaces.CreateSpaceViewModel
import com.chronoio.chrono.ui.spaces.CreateSpaceFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [CreateSpaceDependencies::class],
    modules = [
        CreateSpaceModule::class,
        CreateSpaceModule.Declarations::class
    ]
)
@PerScreen
interface CreateSpaceComponent {
    @Component.Factory
    interface Builder {
        fun create(
            @BindsInstance vmParams: CreateSpaceViewModel.VmParams,
            dependencies: CreateSpaceDependencies
        ): CreateSpaceComponent
    }

    fun inject(fragment: CreateSpaceFragment)
}

@Module
object CreateSpaceModule {
    @Module
    interface Declarations {
        @Binds
        @PerScreen
        fun bindViewModelFactory(factory: CreateSpaceViewModel.Factory): ViewModelProvider.Factory
    }
}

interface CreateSpaceDependencies : ComponentDependencies {
    fun repo(): BlockRepository
    fun analytics(): Analytics
    fun dispatchers(): AppCoroutineDispatchers
    fun spaceManager(): SpaceManager
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun spaceViews(): SpaceViewSubscriptionContainer
    fun userSettingsRepo(): UserSettingsRepository
    fun permissions(): UserPermissionProvider
    fun profileContainer(): ProfileSubscriptionManager
    fun urlBuilder(): UrlBuilder
}