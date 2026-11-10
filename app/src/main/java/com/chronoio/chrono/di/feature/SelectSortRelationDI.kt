package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.presentation.sets.SelectSortRelationViewModel
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.sets.modals.sort.SelectSortRelationFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [SelectSortRelationModule::class])
@PerModal
interface SelectSortRelationSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: SelectSortRelationModule): Builder
        fun build(): SelectSortRelationSubComponent
    }

    fun inject(fragment: SelectSortRelationFragment)
}

@Module
object SelectSortRelationModule {
    @JvmStatic
    @Provides
    @PerModal
    fun provideSelectSortRelationViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        updateDataViewViewer: UpdateDataViewViewer,
        storeOfRelations: StoreOfRelations,
        analytics: Analytics
    ): SelectSortRelationViewModel.Factory = SelectSortRelationViewModel.Factory(
        objectState = state,
        dispatcher = dispatcher,
        updateDataViewViewer = updateDataViewViewer,
        storeOfRelations = storeOfRelations,
        analytics = analytics
    )
}