package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.`object`.DuplicateObject
import com.chronoio.chrono.domain.`object`.UpdateDetail
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationProvider
import com.chronoio.chrono.presentation.relations.providers.ObjectValueProvider
import com.chronoio.chrono.presentation.relations.value.`object`.ObjectValueViewModel
import com.chronoio.chrono.presentation.relations.value.`object`.ObjectValueViewModelFactory
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.relations.value.ObjectValueFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named

//region OBJECT (layout BASIC, TASK etc.)
@PerModal
@Subcomponent(
    modules = [ObjectValueObjectModule::class]
)
interface ObjectValueObjectComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: ObjectValueViewModel.ViewModelParams): Builder
        fun build(): ObjectValueObjectComponent
    }

    fun inject(fragment: ObjectValueFragment)
}

@Module
object ObjectValueObjectModule {

    @JvmStatic
    @PerModal
    @Provides
    fun getSetObjectListIsArchived(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        @Named(ObjectRelationProvider.INTRINSIC_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: UpdateDetail,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        spaceManager: SpaceManager,
        params: ObjectValueViewModel.ViewModelParams,
        objectSearch: SearchObjects,
        urlBuilder: UrlBuilder,
        storeOfObjectTypes: StoreOfObjectTypes,
        objectListIsArchived: SetObjectListIsArchived,
        duplicateObject: DuplicateObject,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations,
        fieldParser: FieldParser,
        spaceViews: SpaceViewSubscriptionContainer
    ): ObjectValueViewModelFactory = ObjectValueViewModelFactory(
        params = params,
        values = values,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        analytics = analytics,
        spaceManager = spaceManager,
        objectSearch = objectSearch,
        urlBuilder = urlBuilder,
        storeOfObjectTypes = storeOfObjectTypes,
        objectListIsArchived = objectListIsArchived,
        duplicateObject = duplicateObject,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        fieldParser = fieldParser,
        spaceViews = spaceViews
    )
}
//endregion

//region SET or COLLECTION (layout SET, COLLECTION )
@PerModal
@Subcomponent(
    modules = [ObjectValueSetModule::class]
)
interface ObjectValueSetComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: ObjectValueViewModel.ViewModelParams): Builder
        fun build(): ObjectValueSetComponent
    }

    fun inject(fragment: ObjectValueFragment)
}

@Module
object ObjectValueSetModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        @Named(ObjectRelationProvider.INTRINSIC_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: UpdateDetail,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        spaceManager: SpaceManager,
        params: ObjectValueViewModel.ViewModelParams,
        objectSearch: SearchObjects,
        urlBuilder: UrlBuilder,
        storeOfObjectTypes: StoreOfObjectTypes,
        objectListIsArchived: SetObjectListIsArchived,
        duplicateObject: DuplicateObject,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations,
        fieldParser: FieldParser,
        spaceViews: SpaceViewSubscriptionContainer
    ): ObjectValueViewModelFactory = ObjectValueViewModelFactory(
        params = params,
        values = values,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        analytics = analytics,
        spaceManager = spaceManager,
        objectSearch = objectSearch,
        urlBuilder = urlBuilder,
        storeOfObjectTypes = storeOfObjectTypes,
        objectListIsArchived = objectListIsArchived,
        duplicateObject = duplicateObject,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        fieldParser = fieldParser,
        spaceViews = spaceViews
    )
}
//endregion

//region DATA VIEW
@PerModal
@Subcomponent(
    modules = [ObjectValueDataViewModule::class]
)
interface ObjectValueDataViewComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun params(params: ObjectValueViewModel.ViewModelParams): Builder
        fun build(): ObjectValueDataViewComponent
    }

    fun inject(fragment: ObjectValueFragment)
}

@Module
object ObjectValueDataViewModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideFactory(
        @Named(ObjectRelationProvider.DATA_VIEW_PROVIDER_TYPE) values: ObjectValueProvider,
        setObjectDetails: UpdateDetail,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        spaceManager: SpaceManager,
        params: ObjectValueViewModel.ViewModelParams,
        objectSearch: SearchObjects,
        urlBuilder: UrlBuilder,
        storeOfObjectTypes: StoreOfObjectTypes,
        objectListIsArchived: SetObjectListIsArchived,
        duplicateObject: DuplicateObject,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations,
        fieldParser: FieldParser,
        spaceViews: SpaceViewSubscriptionContainer
    ): ObjectValueViewModelFactory = ObjectValueViewModelFactory(
        params = params,
        values = values,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        analytics = analytics,
        spaceManager = spaceManager,
        objectSearch = objectSearch,
        urlBuilder = urlBuilder,
        storeOfObjectTypes = storeOfObjectTypes,
        objectListIsArchived = objectListIsArchived,
        duplicateObject = duplicateObject,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        fieldParser = fieldParser,
        spaceViews = spaceViews
    )
}
//endregion