package com.chronoio.chrono.feature_object_type.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.block.interactor.UpdateText
import com.chronoio.chrono.domain.dataview.SetDataViewProperties
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.DuplicateObjects
import com.chronoio.chrono.domain.`object`.GetObject
import com.chronoio.chrono.domain.`object`.SetObjectDetails
import com.chronoio.chrono.domain.objects.DeleteObjects
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.primitives.GetObjectTypeConflictingFields
import com.chronoio.chrono.domain.primitives.SetObjectTypeRecommendedFields
import com.chronoio.chrono.domain.relations.AddToFeaturedRelations
import com.chronoio.chrono.domain.relations.RemoveFromFeaturedRelations
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.templates.CreateTemplate
import com.chronoio.chrono.domain.widgets.CreateWidget
import com.chronoio.chrono.domain.widgets.DeleteWidget
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.feature_object_type.ui.ObjectTypeVmParams
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.navigation.backstack.BackHistoryDelegate
import com.chronoio.chrono.presentation.vault.ExitToVaultDelegate
import com.chronoio.chrono.presentation.editor.cover.CoverImageHashProvider
import com.chronoio.chrono.presentation.util.Dispatcher
import javax.inject.Inject

class ObjectTypeVMFactory @Inject constructor(
    private val vmParams: ObjectTypeVmParams,
    private val analytics: Analytics,
    private val urlBuilder: UrlBuilder,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val userPermissionProvider: UserPermissionProvider,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val storelessSubscriptionContainer: StorelessSubscriptionContainer,
    private val spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider,
    private val fieldParser: FieldParser,
    private val coverImageHashProvider: CoverImageHashProvider,
    private val deleteObjects: DeleteObjects,
    private val setObjectDetails: SetObjectDetails,
    private val stringResourceProvider: StringResourceProvider,
    private val createTemplate: CreateTemplate,
    private val duplicateObjects: DuplicateObjects,
    private val getObjectTypeConflictingFields: GetObjectTypeConflictingFields,
    private val objectTypeSetRecommendedFields: SetObjectTypeRecommendedFields,
    private val setDataViewProperties: SetDataViewProperties,
    private val dispatcher: Dispatcher<Payload>,
    private val setObjectListIsArchived: SetObjectListIsArchived,
    private val createWidget: CreateWidget,
    private val deleteWidget: DeleteWidget,
    private val spaceManager: SpaceManager,
    private val getObject: GetObject,
    private val addToFeaturedRelations: AddToFeaturedRelations,
    private val removeFromFeaturedRelations: RemoveFromFeaturedRelations,
    private val updateText: UpdateText,
    private val searchObjects: SearchObjects,
    private val backHistoryDelegate: BackHistoryDelegate,
    private val exitToVaultDelegate: ExitToVaultDelegate
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ObjectTypeViewModel(
            vmParams = vmParams,
            analytics = analytics,
            urlBuilder = urlBuilder,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            userPermissionProvider = userPermissionProvider,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            spaceSyncAndP2PStatusProvider = spaceSyncAndP2PStatusProvider,
            fieldParser = fieldParser,
            coverImageHashProvider = coverImageHashProvider,
            deleteObjects = deleteObjects,
            setObjectDetails = setObjectDetails,
            stringResourceProvider = stringResourceProvider,
            createTemplate = createTemplate,
            duplicateObjects = duplicateObjects,
            getObjectTypeConflictingFields = getObjectTypeConflictingFields,
            objectTypeSetRecommendedFields = objectTypeSetRecommendedFields,
            setDataViewProperties = setDataViewProperties,
            dispatcher = dispatcher,
            setObjectListIsArchived = setObjectListIsArchived,
            createWidget = createWidget,
            deleteWidget = deleteWidget,
            spaceManager = spaceManager,
            getObject = getObject,
            addToFeaturedRelations = addToFeaturedRelations,
            removeFromFeaturedRelations = removeFromFeaturedRelations,
            updateText = updateText,
            searchObjects = searchObjects,
            backHistoryDelegate = backHistoryDelegate,
            exitToVaultDelegate = exitToVaultDelegate
        ) as T
}