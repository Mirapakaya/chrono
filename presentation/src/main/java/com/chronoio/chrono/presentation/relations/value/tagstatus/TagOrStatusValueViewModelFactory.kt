package com.chronoio.chrono.presentation.relations.value.tagstatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.`object`.UpdateDetail
import com.chronoio.chrono.domain.objects.StoreOfRelationOptions
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.relations.DeleteRelationOptions
import com.chronoio.chrono.domain.relations.SetRelationOptionOrder
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.relations.providers.ObjectValueProvider
import com.chronoio.chrono.presentation.util.Dispatcher
import javax.inject.Inject

class TagOrStatusValueViewModelFactory @Inject constructor(
    private val params: TagOrStatusValueViewModel.ViewModelParams,
    private val values: ObjectValueProvider,
    private val dispatcher: Dispatcher<Payload>,
    private val setObjectDetails: UpdateDetail,
    private val analytics: Analytics,
    private val deleteRelationOptions: DeleteRelationOptions,
    private val setRelationOptionOrder: SetRelationOptionOrder,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfRelationOptions: StoreOfRelationOptions
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ) = TagOrStatusValueViewModel(
        viewModelParams = params,
        values = values,
        dispatcher = dispatcher,
        setObjectDetails = setObjectDetails,
        analytics = analytics,
        deleteRelationOptions = deleteRelationOptions,
        setRelationOptionOrder = setRelationOptionOrder,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        storeOfRelations = storeOfRelations,
        storeOfRelationOptions = storeOfRelationOptions
    ) as T
}