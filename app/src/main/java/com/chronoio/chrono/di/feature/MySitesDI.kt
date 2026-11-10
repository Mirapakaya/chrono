package com.chronoio.chrono.di.feature

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.date.DateFormatter
import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.publishtoweb.MySitesViewModel
import com.chronoio.chrono.ui.publishtoweb.MySitesFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [MySitesDependencies::class],
    modules = [
        MySitesModule::class,
        MySitesModule.Declarations::class
    ]
)
@PerDialog
interface MySitesComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: MySitesViewModel.VmParams,
            dependency: MySitesDependencies
        ): MySitesComponent
    }

    fun inject(fragment: MySitesFragment)
}

@Module
object MySitesModule {

    @Module
    interface Declarations {
        @PerDialog
        @Binds
        fun factory(factory: MySitesViewModel.Factory): ViewModelProvider.Factory

        @PerDialog
        @Binds
        fun dateFormatter(formatter: DateFormatter.Basic): DateFormatter
    }
}

interface MySitesDependencies : ComponentDependencies {
    fun repo(): BlockRepository
    fun auth(): AuthRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun urlBuilder(): UrlBuilder
    fun spaceViews(): SpaceViewSubscriptionContainer
    fun spaceManager(): SpaceManager
}