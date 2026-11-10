package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.UpdateDetail
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.primitives.SetObjectTypeRecommendedFields
import com.chronoio.chrono.domain.relations.AddRelationToObject
import com.chronoio.chrono.domain.relations.AddToFeaturedRelations
import com.chronoio.chrono.domain.relations.DeleteRelationFromObject
import com.chronoio.chrono.domain.relations.RemoveFromFeaturedRelations
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.objects.LockedStateProvider
import com.chronoio.chrono.presentation.relations.ObjectRelationListViewModelFactory
import com.chronoio.chrono.presentation.relations.RelationListViewModel
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationListProvider
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.primitives.ObjectFieldsFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [ObjectRelationListModule::class])
@PerModal
interface ObjectRelationListComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withVmParams(vmParams: RelationListViewModel.VmParams) : Builder
        fun module(module: ObjectRelationListModule): Builder
        fun build(): ObjectRelationListComponent
    }

    fun inject(fragment: ObjectFieldsFragment)
}

@Module
object ObjectRelationListModule {
    @JvmStatic
    @Provides
    @PerModal
    fun factory(
        vmParams: RelationListViewModel.VmParams,
        lockedStateProvider: LockedStateProvider,
        objectRelationListProvider: ObjectRelationListProvider,
        urlBuilder: UrlBuilder,
        dispatcher: Dispatcher<Payload>,
        updateDetail: UpdateDetail,
        addToFeaturedRelations: AddToFeaturedRelations,
        removeFromFeaturedRelations: RemoveFromFeaturedRelations,
        deleteRelationFromObject: DeleteRelationFromObject,
        analytics: Analytics,
        storeOfRelations: StoreOfRelations,
        storeOfObjectTypes: StoreOfObjectTypes,
        addRelationToObject: AddRelationToObject,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        fieldParser: FieldParser,
        userPermissionProvider: UserPermissionProvider,
        setObjectTypeRecommendedFields: SetObjectTypeRecommendedFields
    ): ObjectRelationListViewModelFactory {
        return ObjectRelationListViewModelFactory(
            vmParams = vmParams,
            lockedStateProvider = lockedStateProvider,
            objectRelationListProvider = objectRelationListProvider,
            urlBuilder = urlBuilder,
            dispatcher = dispatcher,
            updateDetail = updateDetail,
            addToFeaturedRelations = addToFeaturedRelations,
            removeFromFeaturedRelations = removeFromFeaturedRelations,
            deleteRelationFromObject = deleteRelationFromObject,
            analytics = analytics,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            addRelationToObject = addRelationToObject,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            fieldParser = fieldParser,
            userPermissionProvider = userPermissionProvider,
            setObjectTypeRecommendedFields = setObjectTypeRecommendedFields
        )
    }

    @JvmStatic
    @Provides
    @PerModal
    fun addToFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): AddToFeaturedRelations =
        AddToFeaturedRelations(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun removeFromFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): RemoveFromFeaturedRelations =
        RemoveFromFeaturedRelations(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun deleteRelationFromObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteRelationFromObject =
        DeleteRelationFromObject(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun provideTypeSetRecommendedFields(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectTypeRecommendedFields = SetObjectTypeRecommendedFields(repo, dispatchers)
}