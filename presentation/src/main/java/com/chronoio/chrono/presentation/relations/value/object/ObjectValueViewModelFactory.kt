package com.chronoio.chrono.presentation.relations.value.`object`

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.`object`.DuplicateObject
import com.chronoio.chrono.domain.`object`.UpdateDetail
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.relations.providers.ObjectValueProvider
import com.chronoio.chrono.presentation.util.Dispatcher
import javax.inject.Inject

class ObjectValueViewModelFactory @Inject constructor(
    private val params: ObjectValueViewModel.ViewModelParams,
    private val values: ObjectValueProvider,
    private val dispatcher: Dispatcher<Payload>,
    private val setObjectDetails: UpdateDetail,
    private val analytics: Analytics,
    private val spaceManager: SpaceManager,
    private val objectSearch: SearchObjects,
    private val urlBuilder: UrlBuilder,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val objectListIsArchived: SetObjectListIsArchived,
    private val duplicateObject: DuplicateObject,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val storeOfRelations: StoreOfRelations,
    private val fieldParser: FieldParser,
    private val spaceViews: SpaceViewSubscriptionContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ) = ObjectValueViewModel(
        viewModelParams = params,
        values = values,
        dispatcher = dispatcher,
        setObjectDetails = setObjectDetails,
        analytics = analytics,
        spaceManager = spaceManager,
        objectSearch = objectSearch,
        urlBuilder = urlBuilder,
        storeOfObjectTypes = storeOfObjectTypes,
        objectListIsArchived = objectListIsArchived,
        duplicateObject = duplicateObject,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        fieldParser = fieldParser,
        spaceViews = spaceViews
    ) as T
}