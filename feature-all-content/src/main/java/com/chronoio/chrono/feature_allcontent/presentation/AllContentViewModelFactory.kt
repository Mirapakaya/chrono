package com.chronoio.chrono.feature_allcontent.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.domain.all_content.RestoreAllContentState
import com.chronoio.chrono.domain.all_content.UpdateAllContentState
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.workspace.RemoveObjectsFromWorkspace
import com.chronoio.chrono.feature_allcontent.presentation.AllContentViewModel.VmParams
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import javax.inject.Inject

class AllContentViewModelFactory @Inject constructor(
    private val vmParams: VmParams,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val urlBuilder: UrlBuilder,
    private val analytics: Analytics,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val storelessSubscriptionContainer: StorelessSubscriptionContainer,
    private val updateAllContentState: UpdateAllContentState,
    private val restoreAllContentState: RestoreAllContentState,
    private val searchObjects: SearchObjects,
    private val localeProvider: LocaleProvider,
    private val createObject: CreateObject,
    private val setObjectListIsArchived: SetObjectListIsArchived,
    private val removeObjectsFromWorkspace: RemoveObjectsFromWorkspace,
    private val userPermissionProvider: UserPermissionProvider,
    private val fieldParser: FieldParser,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AllContentViewModel(
            vmParams = vmParams,
            storeOfObjectTypes = storeOfObjectTypes,
            urlBuilder = urlBuilder,
            analytics = analytics,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            restoreAllContentState = restoreAllContentState,
            updateAllContentState = updateAllContentState,
            searchObjects = searchObjects,
            localeProvider = localeProvider,
            createObject = createObject,
            setObjectListIsArchived = setObjectListIsArchived,
            removeObjectsFromWorkspace = removeObjectsFromWorkspace,
            userPermissionProvider = userPermissionProvider,
            fieldParser = fieldParser,
            spaceViews = spaceViews,
            spaceSyncAndP2PStatusProvider = spaceSyncAndP2PStatusProvider
        ) as T
}