package com.chronoio.chrono.di.feature

import android.content.Context
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.debugging.DebugExportLogs
import com.chronoio.chrono.domain.device.PathProvider
import com.chronoio.chrono.domain.networkmode.GetNetworkMode
import com.chronoio.chrono.domain.networkmode.SetNetworkMode
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.settings.PreferencesViewModel
import com.chronoio.chrono.presentation.util.CopyFileToCacheDirectory
import com.chronoio.chrono.presentation.util.networkModeCopyFileToCacheDirectory
import com.chronoio.chrono.presentation.util.downloader.UriFileProvider
import com.chronoio.chrono.ui.onboarding.OnboardingNetworkSetupDialog
import com.chronoio.chrono.ui.settings.system.PreferenceFragment
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [AppPreferencesDependencies::class],
    modules = [AppPreferencesModule::class]
)
@PerScreen
interface AppPreferencesComponent {

    @Component.Factory
    interface Factory {
        fun create(dependency: AppPreferencesDependencies): AppPreferencesComponent
    }

    fun inject(fragment: PreferenceFragment)
    fun inject(fragment: OnboardingNetworkSetupDialog)
}

@Module
object AppPreferencesModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCopyFileToCache(
        context: Context
    ): CopyFileToCacheDirectory = networkModeCopyFileToCacheDirectory(context)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideLogsExport(
        repository: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): DebugExportLogs = DebugExportLogs(repository, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideViewModelFactory(
        copyFileToCacheDirectory: CopyFileToCacheDirectory,
        getNetworkMode: GetNetworkMode,
        setNetworkMode: SetNetworkMode,
        analytics: Analytics,
        debugExportLogs: DebugExportLogs,
        pathProvider: PathProvider,
        urlFileProvider: UriFileProvider
    ): PreferencesViewModel.Factory = PreferencesViewModel.Factory(
        copyFileToCacheDirectory = copyFileToCacheDirectory,
        getNetworkMode = getNetworkMode,
        setNetworkMode = setNetworkMode,
        analytics = analytics,
        debugExportLogs = debugExportLogs,
        pathProvider = pathProvider,
        uriFileProvider = urlFileProvider
    )
}

interface AppPreferencesDependencies : ComponentDependencies {
    fun context(): Context
    fun dispatchers(): AppCoroutineDispatchers
    fun authRepository(): AuthRepository
    fun analytics(): Analytics
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun pathProvider(): PathProvider
    fun provideUriFileProvider(): UriFileProvider
}