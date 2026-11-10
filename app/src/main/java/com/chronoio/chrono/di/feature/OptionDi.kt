package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.`object`.SetObjectDetails
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.relations.CreateRelationOption
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.relations.option.CreateOrEditOptionViewModel
import com.chronoio.chrono.presentation.relations.option.CreateOrEditOptionViewModelFactory
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationProvider
import com.chronoio.chrono.presentation.relations.providers.ObjectValueProvider
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.relations.value.CreateOrEditOptionFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

//region OBJECT (layout BASIC, TASK etc.)
@PerModal
@Subcomponent(
    modules = [CreateOrEditOptionObjectModule::class]
)
interface CreateOrEditOptionObjectComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: CreateOrEditOptionViewModel.ViewModelParams): Builder
        fun build(): CreateOrEditOptionObjectComponent
    }

    fun inject(fragment: CreateOrEditOptionFragment)
}

@Module
object CreateOrEditOptionObjectModule {

    @JvmStatic
    @Provides
    @PerModal
    fun createRelationOption(
        repo: BlockRepository
    ): CreateRelationOption = CreateRelationOption(repo = repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        params: CreateOrEditOptionViewModel.ViewModelParams,
        @Named(ObjectRelationProvider.INTRINSIC_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: SetObjectDetails,
        dispatcher: Dispatcher<Payload>,
        spaceManager: SpaceManager,
        analytics: Analytics,
        createOption: CreateRelationOption,
        storeOfRelations: StoreOfRelations,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ): CreateOrEditOptionViewModelFactory = CreateOrEditOptionViewModelFactory(
        params = params,
        values = values,
        createOption = createOption,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        spaceManager = spaceManager,
        analytics = analytics,
        storeOfRelations = storeOfRelations,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    )
}
//endregion

//region SET or COLLECTION (layout SET, COLLECTION )
@PerModal
@Subcomponent(
    modules = [CreateOrEditOptionSetModule::class]
)
interface CreateOrEditOptionSetComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: CreateOrEditOptionViewModel.ViewModelParams): Builder
        fun build(): CreateOrEditOptionSetComponent
    }

    fun inject(fragment: CreateOrEditOptionFragment)
}

@Module
object CreateOrEditOptionSetModule {

    @JvmStatic
    @Provides
    @PerModal
    fun createRelationOption(
        repo: BlockRepository
    ): CreateRelationOption = CreateRelationOption(repo = repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        params: CreateOrEditOptionViewModel.ViewModelParams,
        @Named(ObjectRelationProvider.INTRINSIC_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: SetObjectDetails,
        dispatcher: Dispatcher<Payload>,
        spaceManager: SpaceManager,
        analytics: Analytics,
        createOption: CreateRelationOption,
        storeOfRelations: StoreOfRelations,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ): CreateOrEditOptionViewModelFactory = CreateOrEditOptionViewModelFactory(
        params = params,
        values = values,
        createOption = createOption,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        spaceManager = spaceManager,
        analytics = analytics,
        storeOfRelations = storeOfRelations,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    )
}
//endregion

//region DATA VIEW
@PerModal
@Subcomponent(
    modules = [CreateOrEditOptionDataViewModule::class]
)
interface CreateOrEditOptionDataViewComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: CreateOrEditOptionViewModel.ViewModelParams): Builder
        fun build(): CreateOrEditOptionDataViewComponent
    }

    fun inject(fragment: CreateOrEditOptionFragment)
}

@Module
object CreateOrEditOptionDataViewModule {

    @JvmStatic
    @Provides
    @PerModal
    fun createRelationOption(
        repo: BlockRepository
    ): CreateRelationOption = CreateRelationOption(repo = repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        params: CreateOrEditOptionViewModel.ViewModelParams,
        @Named(ObjectRelationProvider.DATA_VIEW_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: SetObjectDetails,
        dispatcher: Dispatcher<Payload>,
        spaceManager: SpaceManager,
        analytics: Analytics,
        createOption: CreateRelationOption,
        storeOfRelations: StoreOfRelations,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ): CreateOrEditOptionViewModelFactory = CreateOrEditOptionViewModelFactory(
        params = params,
        values = values,
        createOption = createOption,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        spaceManager = spaceManager,
        analytics = analytics,
        storeOfRelations = storeOfRelations,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    )
}
//endregion