package com.chronoio.chrono.feature_date.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.GetObject
import com.chronoio.chrono.domain.objects.GetDateObjectByTimestamp
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.relations.GetObjectRelationListById
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.navigation.backstack.BackHistoryDelegate
import com.chronoio.chrono.presentation.vault.ExitToVaultDelegate
import javax.inject.Inject

class DateObjectVMFactory @Inject constructor(
    private val vmParams: DateObjectVmParams,
    private val getObject: GetObject,
    private val analytics: Analytics,
    private val urlBuilder: UrlBuilder,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val userPermissionProvider: UserPermissionProvider,
    private val getObjectRelationListById: GetObjectRelationListById,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val storelessSubscriptionContainer: StorelessSubscriptionContainer,
    private val dateProvider: DateProvider,
    private val spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider,
    private val createObject: CreateObject,
    private val fieldParser: FieldParser,
    private val setObjectListIsArchived: SetObjectListIsArchived,
    private val getDateObjectByTimestamp: GetDateObjectByTimestamp,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val searchObjects: SearchObjects,
    private val backHistoryDelegate: BackHistoryDelegate,
    private val exitToVaultDelegate: ExitToVaultDelegate
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        DateObjectViewModel(
            vmParams = vmParams,
            getObject = getObject,
            analytics = analytics,
            urlBuilder = urlBuilder,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            userPermissionProvider = userPermissionProvider,
            getObjectRelationListById = getObjectRelationListById,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            dateProvider = dateProvider,
            spaceSyncAndP2PStatusProvider = spaceSyncAndP2PStatusProvider,
            createObject = createObject,
            fieldParser = fieldParser,
            setObjectListIsArchived = setObjectListIsArchived,
            getDateObjectByTimestamp = getDateObjectByTimestamp,
            spaceViews = spaceViews,
            searchObjects = searchObjects,
            backHistoryDelegate = backHistoryDelegate,
            exitToVaultDelegate = exitToVaultDelegate
        ) as T
}