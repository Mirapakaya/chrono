package com.chronoio.chrono.di.feature.objects

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.objects.SelectObjectTypeViewModel
import com.chronoio.chrono.ui.objects.creation.SelectObjectTypeBaseFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [SelectObjectTypeDependencies::class],
    modules = [
        SelectObjectTypeModule::class,
        SelectObjectTypeModule.Declarations::class
    ]
)
@PerScreen
interface SelectObjectTypeComponent {
    fun inject(fragment: SelectObjectTypeBaseFragment)
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance params: SelectObjectTypeViewModel.Params,
            dependencies: SelectObjectTypeDependencies
        ): SelectObjectTypeComponent
    }
}

@Module
object SelectObjectTypeModule {
    @Module
    interface Declarations {
        @Binds
        @PerScreen
        fun bindViewModelFactory(factory: SelectObjectTypeViewModel.Factory): ViewModelProvider.Factory
    }
}

interface SelectObjectTypeDependencies : ComponentDependencies {
    fun repo(): BlockRepository
    fun analytics(): Analytics
    fun dispatchers(): AppCoroutineDispatchers
    fun configStorage(): ConfigStorage
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun userSettingsRepository(): UserSettingsRepository
    fun provideSpaceManger(): SpaceManager
    fun provideUrlBuilder(): UrlBuilder
    fun spaceViews(): SpaceViewSubscriptionContainer
}
