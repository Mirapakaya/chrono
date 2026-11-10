package com.chronoio.chrono.di.feature.widgets

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.presentation.widgets.CreateChatObjectViewModel
import com.chronoio.chrono.ui.widgets.CreateChatObjectFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [CreateChatObjectDependencies::class],
    modules = [
        CreateChatObjectModule::class,
        CreateChatObjectModule.Declarations::class
    ]
)
@PerScreen
interface CreateChatObjectComponent {
    @Component.Factory
    interface Builder {
        fun create(
            @BindsInstance vmParams: CreateChatObjectViewModel.VmParams,
            dependencies: CreateChatObjectDependencies
        ): CreateChatObjectComponent
    }

    fun inject(fragment: CreateChatObjectFragment)
}

@Module
object CreateChatObjectModule {
    @Module
    interface Declarations {
        @Binds
        @PerScreen
        fun bindViewModelFactory(factory: CreateChatObjectViewModel.Factory): ViewModelProvider.Factory
    }
}

interface CreateChatObjectDependencies : ComponentDependencies {
    fun analytics(): Analytics
    fun repo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun settings(): UserSettingsRepository
}
