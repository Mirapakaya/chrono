package com.chronoio.chrono.di.feature.sets

import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.presentation.sets.SelectFilterRelationViewModel
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.ui.sets.modals.filter.SelectFilterRelationFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [SelectFilterRelationModule::class])
@PerModal
interface SelectFilterRelationSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: SelectFilterRelationModule): Builder
        fun build(): SelectFilterRelationSubComponent
    }

    fun inject(fragment: SelectFilterRelationFragment)
}

@Module
object SelectFilterRelationModule {
    @JvmStatic
    @Provides
    @PerModal
    fun provideSelectSortRelationViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        storeOfRelations: StoreOfRelations
    ): SelectFilterRelationViewModel.Factory = SelectFilterRelationViewModel.Factory(
        objectState = state,
        storeOfRelations = storeOfRelations
    )
}