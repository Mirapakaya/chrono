package com.chronoio.chrono.di.feature;

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.presentation.sets.sort.ModifyViewerSortViewModel
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.sets.modals.sort.ModifyViewerSortFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [ModifyViewerSortModule::class])
@PerModal
interface ModifyViewerSortSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: ModifyViewerSortModule): Builder
        fun build(): ModifyViewerSortSubComponent
    }

    fun inject(fragment: ModifyViewerSortFragment)
}

@Module
object ModifyViewerSortModule {
    @JvmStatic
    @Provides
    @PerModal
    fun provideViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        updateDataViewViewer: UpdateDataViewViewer,
        analytics: Analytics,
        storeOfRelations: StoreOfRelations
    ): ModifyViewerSortViewModel.Factory = ModifyViewerSortViewModel.Factory(
        state = state,
        dispatcher = dispatcher,
        updateDataViewViewer = updateDataViewViewer,
        analytics = analytics,
        storeOfRelations = storeOfRelations
    )
}
