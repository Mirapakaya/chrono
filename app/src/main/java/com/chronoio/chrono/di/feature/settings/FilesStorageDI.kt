package com.chronoio.chrono.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.presentation.device.BuildProvider
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.account.DeleteAccount
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.device.ClearFileCache
import com.chronoio.chrono.domain.download.FileAutoDownloadSetLimit
import com.chronoio.chrono.domain.download.FileSetAutoDownload
import com.chronoio.chrono.domain.workspace.FileLimitsEventChannel
import com.chronoio.chrono.domain.workspace.InterceptFileLimitEvents
import com.chronoio.chrono.domain.workspace.SpacesUsageInfo
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.settings.FilesStorageViewModel
import com.chronoio.chrono.ui.settings.FilesStorageFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@PerScreen
@Component(
    dependencies = [FilesStorageDependencies::class],
    modules = [
        FilesStorageModule::class,
        FilesStorageModule.Declarations::class
    ]
)
interface FilesStorageComponent {

    @Component.Builder
    interface Builder {

        fun withDependencies(dependency: FilesStorageDependencies): Builder
        fun build(): FilesStorageComponent
    }

    fun inject(fragment: FilesStorageFragment)
}

@Module
object FilesStorageModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun clearFileCache(repo: BlockRepository): ClearFileCache = ClearFileCache(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSpaceUsage(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SpacesUsageInfo = SpacesUsageInfo(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideFileSetAutoDownload(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): FileSetAutoDownload = FileSetAutoDownload(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideFileAutoDownloadSetLimit(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): FileAutoDownloadSetLimit = FileAutoDownloadSetLimit(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideFileLimitEvents(
        channel: FileLimitsEventChannel,
        dispatchers: AppCoroutineDispatchers
    ): InterceptFileLimitEvents = InterceptFileLimitEvents(channel, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDeleteAccountUseCase(
        repo: AuthRepository
    ): DeleteAccount = DeleteAccount(repo = repo)

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: FilesStorageViewModel.Factory): ViewModelProvider.Factory
    }
}

interface FilesStorageDependencies : ComponentDependencies {
    fun blockRepo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun fileEventsChannel(): FileLimitsEventChannel
    fun buildProvider(): BuildProvider
    fun authRepo(): AuthRepository
    fun logger(): Logger
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun userSettingsRepository(): UserSettingsRepository
}