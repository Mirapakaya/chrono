package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.relations.DeleteRelationFromDataView
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.relations.ObjectSetSettingsViewModel
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.sets.modals.ObjectSetSettingsFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [ObjectSetSettingsModule::class])
@PerModal
interface ObjectSetSettingsSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: ObjectSetSettingsModule): Builder
        fun build(): ObjectSetSettingsSubComponent
    }

    fun inject(fragment: ObjectSetSettingsFragment)
}

@Module
object ObjectSetSettingsModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        updateDataViewViewer: UpdateDataViewViewer,
        store: StoreOfRelations,
        analytics: Analytics,
        deleteRelationFromDataView: DeleteRelationFromDataView,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        spaceManager: SpaceManager
    ): ObjectSetSettingsViewModel.Factory = ObjectSetSettingsViewModel.Factory(
        objectState = state,
        dispatcher = dispatcher,
        updateDataViewViewer = updateDataViewViewer,
        analytics = analytics,
        store = store,
        deleteRelationFromDataView = deleteRelationFromDataView,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        spaceManager = spaceManager
    )
}