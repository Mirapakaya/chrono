package com.chronoio.chrono.di.feature.search

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.`object`.DuplicateObject
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelationOptions
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.common.PayloadDelegator
import com.chronoio.chrono.presentation.search.GlobalSearchViewModel
import com.chronoio.chrono.ui.search.GlobalSearchFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [GlobalSearchDependencies::class],
    modules = [
        GlobalSearchModule::class,
        GlobalSearchModule.Declarations::class
    ]
)
@PerScreen
interface GlobalSearchComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: GlobalSearchViewModel.VmParams,
            dependencies: GlobalSearchDependencies
        ): GlobalSearchComponent
    }

    fun inject(fragment: GlobalSearchFragment)
    fun getViewModel(): GlobalSearchViewModel
}

@Module
object GlobalSearchModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDuplicateObject(
        repo: BlockRepository
    ): DuplicateObject = DuplicateObject(repo = repo)

    @Module
    interface Declarations {
        @Binds
        @PerScreen
        fun bindViewModelFactory(factory: GlobalSearchViewModel.Factory): ViewModelProvider.Factory
    }
}

interface GlobalSearchDependencies : ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun repo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun storeOfRelations(): StoreOfRelations
    fun analytics(): Analytics
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun userSettingsRepository(): UserSettingsRepository
    fun fieldParser(): FieldParser
    fun spaceViews(): SpaceViewSubscriptionContainer
    fun storeOfRelationOptions(): StoreOfRelationOptions
    fun deepLinkResolver(): DeepLinkResolver
    fun spaceManager(): SpaceManager
    fun payloadDelegator(): PayloadDelegator
}
