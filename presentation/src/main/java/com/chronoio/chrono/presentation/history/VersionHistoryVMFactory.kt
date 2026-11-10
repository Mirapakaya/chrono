package com.chronoio.chrono.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.domain.history.GetVersions
import com.chronoio.chrono.domain.history.SetVersion
import com.chronoio.chrono.domain.history.ShowVersion
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.presentation.editor.render.DefaultBlockViewRenderer
import com.chronoio.chrono.presentation.history.VersionHistoryViewModel.VmParams
import com.chronoio.chrono.presentation.sets.state.ObjectStateReducer
import javax.inject.Inject

class VersionHistoryVMFactory @Inject constructor(
    private val vmParams: VmParams,
    private val getVersions: GetVersions,
    private val objectSearch: SearchObjects,
    private val dateProvider: DateProvider,
    private val analytics: Analytics,
    private val urlBuilder: UrlBuilder,
    private val showVersion: ShowVersion,
    private val setVersion: SetVersion,
    private val renderer: DefaultBlockViewRenderer,
    private val setStateReducer: ObjectStateReducer,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfObjectTypes: StoreOfObjectTypes
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VersionHistoryViewModel(
            vmParams = vmParams,
            getVersions = getVersions,
            objectSearch = objectSearch,
            dateProvider = dateProvider,
            analytics = analytics,
            urlBuilder = urlBuilder,
            showVersion = showVersion,
            renderer = renderer,
            setVersion = setVersion,
            setStateReducer = setStateReducer,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes
        ) as T
    }
}