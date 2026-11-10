package com.chronoio.chrono.di.feature;

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.presentation.sets.sort.ViewerSortViewModel
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.sets.modals.sort.ViewerSortFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [ViewerSortModule::class])
@PerModal
interface ViewerSortSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: ViewerSortModule): Builder
        fun build(): ViewerSortSubComponent
    }

    fun inject(fragment: ViewerSortFragment)
}

@Module
object ViewerSortModule {
    @JvmStatic
    @Provides
    @PerModal
    fun provideViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        updateDataViewViewer: UpdateDataViewViewer,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics,
        storeOfRelations: StoreOfRelations
    ): ViewerSortViewModel.Factory = ViewerSortViewModel.Factory(
        state = state,
        updateDataViewViewer = updateDataViewViewer,
        dispatcher = dispatcher,
        analytics = analytics,
        storeOfRelations = storeOfRelations
    )
}
