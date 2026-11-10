package com.chronoio.chrono.di.feature.relations

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.RelationFormat
import com.chronoio.chrono.core_utils.di.scope.CreateFromScratch
import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.dataview.interactor.AddRelationToDataView
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.relations.AddRelationToObject
import com.chronoio.chrono.domain.relations.CreateRelation
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.relations.RelationCreateFromScratchForDataViewViewModel
import com.chronoio.chrono.presentation.relations.RelationCreateFromScratchForObjectBlockViewModel
import com.chronoio.chrono.presentation.relations.RelationCreateFromScratchForObjectViewModel
import com.chronoio.chrono.presentation.relations.model.CreateFromScratchState
import com.chronoio.chrono.presentation.relations.model.StateHolder
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.relations.RelationCreateFromScratchForDataViewFragment
import com.chronoio.chrono.ui.relations.RelationCreateFromScratchForObjectBlockFragment
import com.chronoio.chrono.ui.relations.RelationCreateFromScratchForObjectFragment
import com.chronoio.chrono.ui.relations.RelationCreateFromScratchFormatPickerFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [RelationCreateFromScratchForObjectModule::class])
@CreateFromScratch
interface RelationCreateFromScratchForObjectSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: RelationCreateFromScratchForObjectModule): Builder
        fun build(): RelationCreateFromScratchForObjectSubComponent
    }

    fun inject(fragment: RelationCreateFromScratchForObjectFragment)

    fun relationFormatPickerComponent(): RelationFormatPickerSubcomponent.Builder
    fun limitObjectTypeComponent(): LimitObjectTypeSubComponent.Builder
}

@Module
object RelationCreateFromScratchForObjectModule {

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideViewModelFactory(
        addRelationToObject: AddRelationToObject,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        createFromScratchState: StateHolder<CreateFromScratchState>,
        createRelation: CreateRelation,
        spaceManager: SpaceManager,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations
    ) = RelationCreateFromScratchForObjectViewModel.Factory(
        addRelationToObject = addRelationToObject,
        createRelation = createRelation,
        dispatcher = dispatcher,
        analytics = analytics,
        createFromScratchState = createFromScratchState,
        spaceManager = spaceManager,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun createRelation(
        repo: BlockRepository,
        storeOfRelations: StoreOfRelations
    ) = CreateRelation(
        repo = repo,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideState(): StateHolder<CreateFromScratchState> = StateHolder(
        initial = CreateFromScratchState(
            format = RelationFormat.OBJECT,
            limitObjectTypes = emptyList()
        )
    )
}

@Subcomponent(modules = [RelationCreateFromScratchForDataViewModule::class])
@CreateFromScratch
interface RelationCreateFromScratchForDataViewSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: RelationCreateFromScratchForDataViewModule): Builder
        fun build(): RelationCreateFromScratchForDataViewSubComponent
    }

    fun inject(fragment: RelationCreateFromScratchForDataViewFragment)

    fun relationFormatPickerComponent(): RelationFormatPickerSubcomponent.Builder
    fun limitObjectTypeComponent(): LimitObjectTypeSubComponent.Builder
}

@Module
object RelationCreateFromScratchForDataViewModule {

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        updateDataViewViewer: UpdateDataViewViewer,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        createFromScratchState: StateHolder<CreateFromScratchState>,
        createRelation: CreateRelation,
        addRelationToDataView: AddRelationToDataView,
        spaceManager: SpaceManager,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations
    ) = RelationCreateFromScratchForDataViewViewModel.Factory(
        addRelationToDataView = addRelationToDataView,
        dispatcher = dispatcher,
        objectState = state,
        updateDataViewViewer = updateDataViewViewer,
        analytics = analytics,
        createFromScratchState = createFromScratchState,
        createRelation = createRelation,
        spaceManager = spaceManager,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideState(): StateHolder<CreateFromScratchState> = StateHolder(
        initial = CreateFromScratchState(
            format = RelationFormat.OBJECT,
            limitObjectTypes = emptyList()
        )
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun createRelation(
        repo: BlockRepository,
        storeOfRelations: StoreOfRelations
    ) = CreateRelation(
        repo = repo,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun addRelationToDataView(repo: BlockRepository) = AddRelationToDataView(repo)
}

@Subcomponent(modules = [RelationCreateFromScratchForObjectBlockModule::class])
@CreateFromScratch
interface RelationCreateFromScratchForObjectBlockSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: RelationCreateFromScratchForObjectBlockModule): Builder
        fun build(): RelationCreateFromScratchForObjectBlockSubComponent
    }

    fun inject(fragment: RelationCreateFromScratchForObjectBlockFragment)

    fun relationFormatPickerComponent(): RelationFormatPickerSubcomponent.Builder
    fun limitObjectTypeComponent(): LimitObjectTypeSubComponent.Builder
}

@Module
object RelationCreateFromScratchForObjectBlockModule {

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideViewModelFactory(
        addRelationToObject: AddRelationToObject,
        createRelation: CreateRelation,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        createFromScratchState: StateHolder<CreateFromScratchState>,
        spaceManager: SpaceManager,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        storeOfRelations: StoreOfRelations
    ) = RelationCreateFromScratchForObjectBlockViewModel.Factory(
        addRelationToObject = addRelationToObject,
        createRelation = createRelation,
        dispatcher = dispatcher,
        analytics = analytics,
        createFromScratchState = createFromScratchState,
        spaceManager = spaceManager,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideState(): StateHolder<CreateFromScratchState> = StateHolder(
        initial = CreateFromScratchState(
            format = RelationFormat.OBJECT,
            limitObjectTypes = emptyList()
        )
    )

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun createRelation(
        repo: BlockRepository,
        storeOfRelations: StoreOfRelations
    ) = CreateRelation(
        repo = repo,
        storeOfRelations = storeOfRelations
    )
}

@Subcomponent
@PerDialog
interface RelationFormatPickerSubcomponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): RelationFormatPickerSubcomponent
    }

    fun inject(fragment: RelationCreateFromScratchFormatPickerFragment)
}

