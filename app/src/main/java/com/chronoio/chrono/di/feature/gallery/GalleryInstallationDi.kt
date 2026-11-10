package com.chronoio.chrono.di.feature.gallery

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.data.auth.event.EventProcessImportDateChannel
import com.chronoio.chrono.data.auth.event.EventProcessImportRemoteChannel
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.workspace.EventProcessImportChannel
import com.chronoio.chrono.gallery_experience.viewmodel.GalleryInstallationViewModel
import com.chronoio.chrono.gallery_experience.viewmodel.GalleryInstallationViewModelFactory
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.middleware.interactor.EventProcessImportMiddlewareChannel
import com.chronoio.chrono.presentation.spaces.SpaceGradientProvider
import com.chronoio.chrono.ui.gallery.GalleryInstallationFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [GalleryInstallationComponentDependencies::class],
    modules = [
        GalleryInstallationModule::class,
        GalleryInstallationModule.Declarations::class
    ]
)
@PerScreen
interface GalleryInstallationComponent {

    @Component.Builder
    interface Builder {
        fun withDependencies(dependencies: GalleryInstallationComponentDependencies): Builder

        @BindsInstance
        fun withParams(params: GalleryInstallationViewModel.ViewModelParams): Builder
        fun build(): GalleryInstallationComponent
    }

    fun inject(fragment: GalleryInstallationFragment)
}

@Module
object GalleryInstallationModule {

    @Provides
    @PerScreen
    fun provideEventProcessRemoteChannel(
        proxy: EventProxy
    ): EventProcessImportRemoteChannel = EventProcessImportMiddlewareChannel(events = proxy)

    @Provides
    @PerScreen
    fun provideEventProcessDateChannel(
        channel: EventProcessImportRemoteChannel
    ): EventProcessImportChannel = EventProcessImportDateChannel(channel = channel)

    @Provides
    @PerScreen
    fun provideGradientProvider(): SpaceGradientProvider {
        return SpaceGradientProvider.Default
    }

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: GalleryInstallationViewModelFactory
        ): ViewModelProvider.Factory

    }
}

interface GalleryInstallationComponentDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun appCoroutineDispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun userPermissionProvider(): UserPermissionProvider
    fun eventProxy(): EventProxy
    fun configStorage(): ConfigStorage
}