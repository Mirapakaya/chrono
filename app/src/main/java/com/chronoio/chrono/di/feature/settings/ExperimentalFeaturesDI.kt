package com.chronoio.chrono.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.presentation.settings.ExperimentalFeaturesViewModel
import com.chronoio.chrono.ui.settings.ExperimentalFeaturesFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [ExperimentalFeaturesDependencies::class],
    modules = [
        ExperimentalFeaturesModule::class,
        ExperimentalFeaturesModule.Declarations::class
    ]
)
@PerScreen
interface ExperimentalFeaturesComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: ExperimentalFeaturesDependencies): ExperimentalFeaturesComponent
    }

    fun inject(fragment: ExperimentalFeaturesFragment)
}

@Module
object ExperimentalFeaturesModule {
    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: ExperimentalFeaturesViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface ExperimentalFeaturesDependencies : ComponentDependencies {
    fun userSettingsRepository(): UserSettingsRepository
}
