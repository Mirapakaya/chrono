package com.chronoio.chrono.di.feature

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.feature_properties.space.SpacePropertiesViewModel
import com.chronoio.chrono.presentation.publishtoweb.PublishToWebViewModel
import com.chronoio.chrono.ui.publishtoweb.PublishToWebFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [PublishToWebDependencies::class],
    modules = [
        PublishToWebModule::class,
        PublishToWebModule.Declarations::class
    ]
)
@PerDialog
interface PublishToWebComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: PublishToWebViewModel.Params,
            dependency: PublishToWebDependencies
        ): PublishToWebComponent
    }

    fun inject(fragment: PublishToWebFragment)
}

@Module
object PublishToWebModule {

    @Module
    interface Declarations {
        @PerDialog
        @Binds
        fun factory(factory: PublishToWebViewModel.Factory): ViewModelProvider.Factory
    }
}

interface PublishToWebDependencies : ComponentDependencies {
    fun repo(): BlockRepository
    fun auth(): AuthRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun spaces(): SpaceViewSubscriptionContainer
    fun urlBuilder(): UrlBuilder
}
