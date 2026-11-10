package com.chronoio.chrono.di.feature.spaces

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.spaces.ManageSectionsViewModel
import com.chronoio.chrono.ui.settings.space.ManageSectionsFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [ManageSectionsDependencies::class],
    modules = [ManageSectionsModule.Bindings::class]
)
@PerScreen
interface ManageSectionsComponent {
    @Component.Factory
    interface Factory {
        fun create(
            dependencies: ManageSectionsDependencies
        ): ManageSectionsComponent
    }

    fun inject(fragment: ManageSectionsFragment)
}

@Module
object ManageSectionsModule {

    @Module
    interface Bindings {
        @PerScreen
        @Binds
        fun factory(factory: ManageSectionsViewModel.Factory): ViewModelProvider.Factory
    }
}

interface ManageSectionsDependencies : ComponentDependencies {
    fun spaceManager(): SpaceManager
    fun settings(): UserSettingsRepository
    fun dispatchers(): AppCoroutineDispatchers
}
