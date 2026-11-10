package com.chronoio.chrono.di.feature.spaces

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.presentation.spaces.SpaceListViewModel
import com.chronoio.chrono.ui.spaces.SpaceListFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [SpaceListDependencies::class],
    modules = [
        SpaceListModule::class,
        SpaceListModule.Declarations::class
    ]
)
@PerScreen
interface SpaceListComponent {
    @Component.Factory
    interface Builder {
        fun create(dependencies: SpaceListDependencies): SpaceListComponent
    }
    fun inject(fragment: SpaceListFragment)
}

@Module
object SpaceListModule {
    @Module
    interface Declarations {
        @Binds
        @PerScreen
        fun bindViewModelFactory(factory: SpaceListViewModel.Factory): ViewModelProvider.Factory
    }
}

interface SpaceListDependencies : ComponentDependencies {
    fun spaceViewContainer(): SpaceViewSubscriptionContainer
    fun permissions(): UserPermissionProvider
    fun urlBuilder(): UrlBuilder
    fun repo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
}