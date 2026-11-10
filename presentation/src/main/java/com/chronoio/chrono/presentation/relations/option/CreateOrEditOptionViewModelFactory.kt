package com.chronoio.chrono.presentation.relations.option

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.`object`.SetObjectDetails
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.relations.CreateRelationOption
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.relations.providers.ObjectValueProvider
import com.chronoio.chrono.presentation.util.Dispatcher
import javax.inject.Inject

class CreateOrEditOptionViewModelFactory  @Inject constructor(
    private val params: CreateOrEditOptionViewModel.ViewModelParams,
    private val values: ObjectValueProvider,
    private val createOption: CreateRelationOption,
    private val setObjectDetails: SetObjectDetails,
    private val dispatcher: Dispatcher<Payload>,
    private val spaceManager: SpaceManager,
    private val analytics: Analytics,
    private val storeOfRelations: StoreOfRelations,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ) = CreateOrEditOptionViewModel(
        viewModelParams = params,
        values = values,
        createOption = createOption,
        setObjectDetails = setObjectDetails,
        dispatcher = dispatcher,
        spaceManager = spaceManager,
        analytics = analytics,
        storeOfRelations = storeOfRelations,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
    ) as T
}