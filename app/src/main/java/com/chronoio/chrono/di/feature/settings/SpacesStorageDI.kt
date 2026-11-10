package com.chronoio.chrono.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.interactor.GetAccount
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.search.SubscriptionEventChannel
import com.chronoio.chrono.domain.workspace.FileLimitsEventChannel
import com.chronoio.chrono.domain.workspace.SpacesUsageInfo
import com.chronoio.chrono.domain.workspace.InterceptFileLimitEvents
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.membership.provider.MembershipProvider
import com.chronoio.chrono.presentation.settings.SpacesStorageViewModel
import com.chronoio.chrono.presentation.settings.SpacesStorageViewModelFactory
import com.chronoio.chrono.ui.settings.SpacesStorageFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@PerScreen
@Component(
    dependencies = [SpacesStorageDependencies::class],
    modules = [
        SpacesStorageModule::class,
        SpacesStorageModule.Declarations::class
    ]
)
interface SpacesStorageComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: SpacesStorageViewModel.VmParams,
            dependency: SpacesStorageDependencies
        ): SpacesStorageComponent
    }

    fun inject(fragment: SpacesStorageFragment)
}

@Module
object SpacesStorageModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideStoreLessSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): StorelessSubscriptionContainer = StorelessSubscriptionContainer.Impl(
        repo = repo,
        channel = channel,
        dispatchers = dispatchers,
        logger = logger
    )

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
    fun provideFileLimitEvents(
        channel: FileLimitsEventChannel,
        dispatchers: AppCoroutineDispatchers
    ) : InterceptFileLimitEvents = InterceptFileLimitEvents(channel, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetAccountUseCase(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetAccount = GetAccount(repo = repo, dispatcher = dispatchers)

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: SpacesStorageViewModelFactory): ViewModelProvider.Factory
    }
}

interface SpacesStorageDependencies : ComponentDependencies {
    fun blockRepo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun configStorage(): ConfigStorage
    fun channel(): SubscriptionEventChannel
    fun fileEventsChannel(): FileLimitsEventChannel
    fun authRepo(): AuthRepository
    fun logger(): Logger
    fun spaceManager(): SpaceManager
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun membershipProvider(): MembershipProvider
    fun userPermissionProvider(): UserPermissionProvider
}