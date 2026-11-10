package com.chronoio.chrono.di.feature.sets;

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.objects.options.GetOptions
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.sets.ObjectSetDatabase
import com.chronoio.chrono.presentation.sets.filter.FilterViewModel
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.sets.modals.filter.CreateFilterFromInputFieldValueFragment
import com.chronoio.chrono.ui.sets.modals.filter.CreateFilterFromSelectedValueFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [CreateFilterModule::class])
@PerModal
interface CreateFilterSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: CreateFilterModule): Builder
        fun build(): CreateFilterSubComponent
    }

    fun inject(fragment: CreateFilterFromSelectedValueFragment)
    fun inject(fragment: CreateFilterFromInputFieldValueFragment)
    fun createPickConditionComponent(): PickFilterConditionSubComponent.Builder
}

@Module
object CreateFilterModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        updateDataViewViewer: UpdateDataViewViewer,
        searchObjects: SearchObjects,
        urlBuilder: UrlBuilder,
        storeOfObjectTypes: StoreOfObjectTypes,
        storeOfRelations: StoreOfRelations,
        objectSetDatabase: ObjectSetDatabase,
        analytics: Analytics,
        getOptions: GetOptions,
        spaceManager: SpaceManager,
        fieldParser: FieldParser,
        spaceViews: SpaceViewSubscriptionContainer,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ): FilterViewModel.Factory = FilterViewModel.Factory(
        objectState = state,
        dispatcher = dispatcher,
        updateDataViewViewer = updateDataViewViewer,
        searchObjects = searchObjects,
        urlBuilder = urlBuilder,
        storeOfObjectTypes = storeOfObjectTypes,
        storeOfRelations = storeOfRelations,
        objectSetDatabase = objectSetDatabase,
        analytics = analytics,
        getOptions = getOptions,
        spaceManager = spaceManager,
        fieldParser = fieldParser,
        spaceViews = spaceViews,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    )
}
