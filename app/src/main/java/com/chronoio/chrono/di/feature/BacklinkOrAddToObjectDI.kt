package com.chronoio.chrono.di.feature

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.interactor.sets.GetObjectTypes
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.linking.BackLinkOrAddToObjectViewModelFactory
import com.chronoio.chrono.presentation.search.ObjectSearchViewModel
import com.chronoio.chrono.ui.linking.BacklinkOrAddToObjectFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides


@PerScreen
@Component(
    dependencies = [BacklinkOrAddToObjectDependencies::class],
    modules = [
        BackLinkToObjectModule::class,
        BackLinkToObjectModule.Declarations::class
    ]
)
interface BacklinkOrAddToObjectComponent {

    @Component.Builder
    interface Builder {

        fun withDependencies(dependency: BacklinkOrAddToObjectDependencies): Builder

        @BindsInstance
        fun withParams(params: ObjectSearchViewModel.VmParams): Builder

        fun build(): BacklinkOrAddToObjectComponent
    }

    fun inject(fragment: BacklinkOrAddToObjectFragment)
}

@Module
object BackLinkToObjectModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetObjectTypesUseCase(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetObjectTypes = GetObjectTypes(repository, dispatchers)

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: BackLinkOrAddToObjectViewModelFactory): ViewModelProvider.Factory
    }
}

interface BacklinkOrAddToObjectDependencies : ComponentDependencies {
    fun authRepository(): AuthRepository
    fun blockRepository(): BlockRepository
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
    fun searchObjects(): SearchObjects
    fun dateProvider(): DateProvider
    fun fieldParser(): FieldParser
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun spaceViews(): SpaceViewSubscriptionContainer
}
