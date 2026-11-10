package com.chronoio.chrono.di.feature.search

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.chats.ChatsDetailsSubscriptionContainer
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.multiplayer.ParticipantSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.objects.CrossSpaceObjectTypesContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.workspace.DeepLinkToObjectDelegate
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.feature_search.presentation.SearchViewModel
import com.chronoio.chrono.ui.search.v2.SearchV2Fragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [SearchV2Dependencies::class],
    modules = [
        SearchV2Module::class,
        SearchV2Module.Declarations::class
    ]
)
@PerScreen
interface SearchV2Component {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: SearchViewModel.VmParams,
            dependencies: SearchV2Dependencies
        ): SearchV2Component
    }

    fun inject(fragment: SearchV2Fragment)

    /** For embedded use (the chat attach-object sheet). */
    fun searchViewModel(): SearchViewModel
}

@Module
object SearchV2Module {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDeepLinkToObjectDelegate(
        default: DeepLinkToObjectDelegate.Default
    ): DeepLinkToObjectDelegate = default

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSearchViewModel(
        factory: SearchViewModel.Factory
    ): SearchViewModel = factory.create(SearchViewModel::class.java)

    @Module
    interface Declarations {
        @Binds
        @PerScreen
        fun bindViewModelFactory(factory: SearchViewModel.Factory): ViewModelProvider.Factory
    }
}

interface SearchV2Dependencies : ComponentDependencies {
    fun repo(): BlockRepository
    fun userSettingsRepository(): UserSettingsRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun urlBuilder(): UrlBuilder
    fun fieldParser(): FieldParser
    fun configStorage(): ConfigStorage
    fun spaceManager(): SpaceManager
    fun userPermissionProvider(): UserPermissionProvider
    fun spaceViews(): SpaceViewSubscriptionContainer
    fun participants(): ParticipantSubscriptionContainer
    fun chatsDetails(): ChatsDetailsSubscriptionContainer
    fun crossSpaceObjectTypes(): CrossSpaceObjectTypesContainer
    fun storeOfObjectTypes(): StoreOfObjectTypes
}
