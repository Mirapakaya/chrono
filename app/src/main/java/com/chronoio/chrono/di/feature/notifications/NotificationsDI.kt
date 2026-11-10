package com.chronoio.chrono.di.feature.notifications

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.notifications.NotificationsProvider
import com.chronoio.chrono.presentation.notifications.NotificationsViewModelFactory
import com.chronoio.chrono.ui.notifications.NotificationsFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [NotificationDependencies::class],
    modules = [
        NotificationsModule::class,
        NotificationsModule.Declarations::class
    ]
)
@PerDialog
interface NotificationComponent {
    @Component.Factory
    interface Factory {
        fun create(dependency: NotificationDependencies): NotificationComponent
    }

    fun inject(fragment: NotificationsFragment)
}

@Module
object NotificationsModule {
    @Module
    interface Declarations {
        @PerDialog
        @Binds
        fun factory(factory: NotificationsViewModelFactory): ViewModelProvider.Factory
    }
}

interface NotificationDependencies : ComponentDependencies {
    fun repo(): BlockRepository
    fun analytics(): Analytics
    fun notificationsProvider(): NotificationsProvider
    fun spaceManager(): SpaceManager
    fun userSettingsRepository(): UserSettingsRepository
    fun provideDispatchers(): AppCoroutineDispatchers
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
}