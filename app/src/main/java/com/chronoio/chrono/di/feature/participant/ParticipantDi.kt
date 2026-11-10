package com.chronoio.chrono.di.feature.participant

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.icon.SetDocumentImageIcon
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.RemoveSpaceMembers
import com.chronoio.chrono.domain.multiplayer.SearchOneToOneChatByIdentity
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.SetObjectDetails
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SubscriptionEventChannel
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.membership.provider.MembershipProvider
import com.chronoio.chrono.presentation.profile.ParticipantViewModel
import com.chronoio.chrono.ui.profile.ParticipantFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [ParticipantComponentDependencies::class],
    modules = [
        ParticipantModule::class,
        ParticipantModule.Declarations::class
    ]
)
@PerScreen
interface ParticipantComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: ParticipantViewModel.VmParams,
            dependencies: ParticipantComponentDependencies
        ): ParticipantComponent
    }

    fun inject(fragment: ParticipantFragment)

}

@Module
object ParticipantModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetObjectDetails(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(
        repo,
        dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDocumentImageIconUseCase(
        repo: BlockRepository
    ): SetDocumentImageIcon = SetDocumentImageIcon(repo)

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
    fun provideSearchOneToOneChatByIdentity(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SearchOneToOneChatByIdentity = SearchOneToOneChatByIdentity(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRemoveSpaceMembers(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): RemoveSpaceMembers = RemoveSpaceMembers(
        repo = repo,
        dispatchers = dispatchers
    )

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: ParticipantViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface ParticipantComponentDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun userSettingsRepository(): UserSettingsRepository
    fun logger(): Logger
    fun localeProvider(): LocaleProvider
    fun userPermissionProvider(): UserPermissionProvider
    fun fieldsProvider(): FieldParser
    fun provideMembershipProvider(): MembershipProvider
    fun subEventChannel(): SubscriptionEventChannel
    fun provideConfigStorage(): ConfigStorage
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun spaceManager(): SpaceManager
}