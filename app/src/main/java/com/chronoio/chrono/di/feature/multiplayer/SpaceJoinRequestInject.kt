package com.chronoio.chrono.di.feature.multiplayer

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.membership.provider.MembershipProvider
import com.chronoio.chrono.presentation.multiplayer.SpaceJoinRequestViewModel
import com.chronoio.chrono.ui.multiplayer.SpaceJoinRequestFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [SpaceJoinRequestDependencies::class],
    modules = [
        SpaceJoinRequestModule::class,
        SpaceJoinRequestModule.Declarations::class
    ]
)
@PerDialog
interface SpaceJoinRequestComponent {

    @Component.Builder
    interface Builder {
        fun withDependencies(dependencies: SpaceJoinRequestDependencies): Builder
        @BindsInstance
        fun withParams(params: SpaceJoinRequestViewModel.VmParams): Builder
        fun build(): SpaceJoinRequestComponent
    }

    fun inject(fragment: SpaceJoinRequestFragment)
}

@Module
object SpaceJoinRequestModule {

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: SpaceJoinRequestViewModel.Factory): ViewModelProvider.Factory
    }

}

interface SpaceJoinRequestDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun permissions(): UserPermissionProvider
    fun provideSpaceViewContainer(): SpaceViewSubscriptionContainer
    fun provideMembershipProvider(): MembershipProvider
}