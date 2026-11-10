package com.chronoio.chrono.feature_properties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.dataview.SetDataViewProperties
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.`object`.SetObjectDetails
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.primitives.SetObjectTypeRecommendedFields
import com.chronoio.chrono.domain.relations.CreateRelation
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.feature_properties.add.EditTypePropertiesVmParams
import com.chronoio.chrono.presentation.util.Dispatcher
import javax.inject.Inject

class EditTypePropertiesViewModelFactory @Inject constructor(
    private val vmParams: EditTypePropertiesVmParams,
    private val storeOfRelations: StoreOfRelations,
    private val stringResourceProvider: StringResourceProvider,
    private val createRelation: CreateRelation,
    private val setObjectDetails: SetObjectDetails,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val setObjectTypeRecommendedFields: SetObjectTypeRecommendedFields,
    private val setDataViewProperties: SetDataViewProperties,
    private val dispatcher: Dispatcher<Payload>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        EditTypePropertiesViewModel(
            vmParams = vmParams,
            storeOfRelations = storeOfRelations,
            stringResourceProvider = stringResourceProvider,
            createRelation = createRelation,
            setObjectDetails = setObjectDetails,
            storeOfObjectTypes = storeOfObjectTypes,
            setObjectTypeRecommendedFields = setObjectTypeRecommendedFields,
            setDataViewProperties = setDataViewProperties,
            dispatcher = dispatcher
        ) as T
}