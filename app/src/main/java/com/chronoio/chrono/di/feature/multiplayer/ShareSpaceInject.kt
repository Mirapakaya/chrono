package com.chronoio.chrono.di.feature.multiplayer

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.clipboard.Clipboard
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.TechSpaceProvider
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.invite.SpaceInviteLinkStore
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.ProfileSubscriptionManager
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.membership.provider.MembershipProvider
import com.chronoio.chrono.presentation.multiplayer.ShareSpaceViewModel
import com.chronoio.chrono.presentation.spaces.SpaceGradientProvider
import com.chronoio.chrono.ui.multiplayer.ShareSpaceFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [ShareSpaceDependencies::class],
    modules = [
        ShareSpaceModule::class,
        ShareSpaceModule.Declarations::class
    ]
)
@PerDialog
interface ShareSpaceComponent {
    @Component.Builder
    interface Builder {
        fun withDependencies(dependencies: ShareSpaceDependencies): Builder
        @BindsInstance
        fun withParams(params: ShareSpaceViewModel.VmParams): Builder
        fun build(): ShareSpaceComponent
    }

    fun inject(fragment: ShareSpaceFragment)
}

@Module
object ShareSpaceModule {

    @PerDialog
    @Provides
    fun gradientProvider(): SpaceGradientProvider = SpaceGradientProvider.Default

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: ShareSpaceViewModel.Factory): ViewModelProvider.Factory
    }
}

interface ShareSpaceDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun auth() : AuthRepository
    fun urlBuilder(): UrlBuilder
    fun spaceManager(): SpaceManager
    fun dispatchers(): AppCoroutineDispatchers
    fun container(): StorelessSubscriptionContainer
    fun config(): ConfigStorage
    fun techSpaceProvider(): TechSpaceProvider
    fun permissions(): UserPermissionProvider
    fun analytics(): Analytics
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun provideMembershipProvider(): MembershipProvider
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun clipboard(): Clipboard
    fun logger(): Logger
    fun spaceInviteLinkStore(): SpaceInviteLinkStore
    fun stringResourceProvider(): StringResourceProvider
    fun profileContainer(): ProfileSubscriptionManager
}