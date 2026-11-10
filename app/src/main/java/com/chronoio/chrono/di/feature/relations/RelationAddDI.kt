package com.chronoio.chrono.di.feature.relations

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.dataview.interactor.AddRelationToDataView
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.relations.AddRelationToObject
import com.chronoio.chrono.domain.relations.GetRelations
import com.chronoio.chrono.domain.workspace.AddObjectToWorkspace
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.relations.RelationAddToDataViewViewModel
import com.chronoio.chrono.presentation.relations.RelationAddToObjectViewModel
import com.chronoio.chrono.presentation.relations.RelationAddViewModelBase
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationProvider
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationProvider.Companion.DATA_VIEW_PROVIDER_TYPE
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationProvider.Companion.INTRINSIC_PROVIDER_TYPE
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.relations.RelationAddToDataViewFragment
import com.chronoio.chrono.ui.relations.RelationAddToObjectBlockFragment
import com.chronoio.chrono.ui.relations.RelationAddToObjectFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [RelationAddToObjectModule::class])
@PerDialog
interface RelationAddToObjectSubComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withVmParams(vmParams: RelationAddViewModelBase.VmParams) : Builder
        fun module(module: RelationAddToObjectModule): Builder
        fun build(): RelationAddToObjectSubComponent
    }

    fun inject(fragment: RelationAddToObjectFragment)
    fun inject(fragment: RelationAddToObjectBlockFragment)
}

@Module
object RelationAddToObjectModule {
    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        vmParams: RelationAddViewModelBase.VmParams,
        addRelationToObject: AddRelationToObject,
        storeOfRelations: StoreOfRelations,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        @Named(INTRINSIC_PROVIDER_TYPE) relationsProvider: ObjectRelationProvider,
        getRelations: GetRelations,
        appCoroutineDispatchers: AppCoroutineDispatchers,
        addObjectToWorkspace: AddObjectToWorkspace,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ): RelationAddToObjectViewModel.Factory = RelationAddToObjectViewModel.Factory(
        vmParams = vmParams,
        storeOfRelations = storeOfRelations,
        addRelationToObject = addRelationToObject,
        dispatcher = dispatcher,
        analytics = analytics,
        relationsProvider = relationsProvider,
        getRelations = getRelations,
        appCoroutineDispatchers = appCoroutineDispatchers,
        addObjectToWorkspace = addObjectToWorkspace,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun getRelations(repo: BlockRepository) : GetRelations = GetRelations(repo)

    @JvmStatic
    @Provides
    @PerDialog
    fun addObjectToWorkspace(
        repo: BlockRepository,
        appCoroutineDispatchers: AppCoroutineDispatchers
    ) : AddObjectToWorkspace = AddObjectToWorkspace(
        repo = repo,
        dispatchers = appCoroutineDispatchers
    )
}

@Subcomponent(modules = [RelationAddToDataViewModule::class])
@PerDialog
interface RelationAddToDataViewSubComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withVmParams(vmParams: RelationAddViewModelBase.VmParams) : Builder
        fun module(module: RelationAddToDataViewModule): Builder
        fun build(): RelationAddToDataViewSubComponent
    }

    fun inject(fragment: RelationAddToDataViewFragment)
}

@Module
object RelationAddToDataViewModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        vmParams: RelationAddViewModelBase.VmParams,
        addRelationToDataView: AddRelationToDataView,
        dispatcher: Dispatcher<Payload>,
        state: MutableStateFlow<ObjectState>,
        updateDataViewViewer: UpdateDataViewViewer,
        analytics: Analytics,
        @Named(DATA_VIEW_PROVIDER_TYPE) relationsProvider: ObjectRelationProvider,
        appCoroutineDispatchers: AppCoroutineDispatchers,
        getRelations: GetRelations,
        addObjectToWorkspace: AddObjectToWorkspace,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations
    ): RelationAddToDataViewViewModel.Factory = RelationAddToDataViewViewModel.Factory(
        addRelationToDataView = addRelationToDataView,
        dispatcher = dispatcher,
        state = state,
        updateDataViewViewer = updateDataViewViewer,
        analytics = analytics,
        relationsProvider = relationsProvider,
        appCoroutineDispatchers = appCoroutineDispatchers,
        getRelations = getRelations,
        addObjectToWorkspace = addObjectToWorkspace,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        vmParams = vmParams
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideAddRelationToDataViewUseCase(
        repo: BlockRepository
    ): AddRelationToDataView = AddRelationToDataView(repo)

    @JvmStatic
    @Provides
    @PerDialog
    fun getRelations(repo: BlockRepository) : GetRelations = GetRelations(repo)


    @JvmStatic
    @Provides
    @PerDialog
    fun addObjectToWorkspace(
        repo: BlockRepository,
        appCoroutineDispatchers: AppCoroutineDispatchers
    ) : AddObjectToWorkspace = AddObjectToWorkspace(
        repo = repo,
        dispatchers = appCoroutineDispatchers
    )
}