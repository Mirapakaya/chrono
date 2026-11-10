package com.chronoio.chrono.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.device.PathProvider
import com.chronoio.chrono.presentation.settings.DebugViewModel
import com.chronoio.chrono.presentation.util.downloader.UriFileProvider
import com.chronoio.chrono.ui.settings.DebugFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [DebugDependencies::class],
    modules = [
        DebugModule::class,
        DebugModule.Declarations::class
    ]
)
@PerScreen
interface DebugComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: DebugDependencies): DebugComponent
    }

    fun inject(fragment: DebugFragment)
}

@Module
object DebugModule {
    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: DebugViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface DebugDependencies : ComponentDependencies {
    fun path(): PathProvider
    fun auth(): AuthRepository
    fun repo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun uriFileProvider(): UriFileProvider
    fun userSettingsRepository(): UserSettingsRepository
}