package com.chronoio.chrono.di.feature

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.download.DownloadFile
import com.chronoio.chrono.domain.download.Downloader
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.presentation.media.MediaViewModel
import com.chronoio.chrono.ui.media.MediaActivity
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Component(
    dependencies = [MediaDependencies::class],
    modules = [
        MediaModule::class,
        MediaModule.Declarations::class
    ]
)
@PerScreen
interface MediaComponent {

    @Component.Factory
    interface Factory {
        fun create(dependency: MediaDependencies): MediaComponent
    }

    fun inject(activity: MediaActivity)
}

@Module
object MediaModule {

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun factory(factory: MediaViewModel.Factory): ViewModelProvider.Factory
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDownloadFileUseCase(
        downloader: Downloader
    ): DownloadFile = DownloadFile(
        downloader = downloader,
        context = Dispatchers.IO
    )
}

interface MediaDependencies : ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun repo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun downloader(): Downloader
    fun userSettings(): UserSettingsRepository
}