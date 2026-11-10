package com.chronoio.chrono.di.feature;

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.presentation.sets.ObjectSetDatabase
import com.chronoio.chrono.presentation.sets.filter.ViewerFilterViewModel
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.sets.ViewerFilterFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [ViewerFilterModule::class])
@ViewerFilterByScope
interface ViewerFilterSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: ViewerFilterModule): Builder
        fun build(): ViewerFilterSubComponent
    }

    fun inject(fragment: ViewerFilterFragment)
}

@Module
object ViewerFilterModule {

    @JvmStatic
    @Provides
    @ViewerFilterByScope
    fun provideViewerFilterViewModelFactory(
        state: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        updateDataViewViewer: UpdateDataViewViewer,
        urlBuilder: UrlBuilder,
        analytics: Analytics,
        storeOfRelations: StoreOfRelations,
        db: ObjectSetDatabase,
        fieldParser: FieldParser,
        storeOfObjectTypes: StoreOfObjectTypes
    ): ViewerFilterViewModel.Factory = ViewerFilterViewModel.Factory(
        state = state,
        dispatcher = dispatcher,
        updateDataViewViewer = updateDataViewViewer,
        urlBuilder = urlBuilder,
        analytics = analytics,
        storeOfRelations = storeOfRelations,
        objectSetDatabase = db,
        fieldParser = fieldParser,
        storeOfObjectTypes = storeOfObjectTypes
    )
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewerFilterByScope