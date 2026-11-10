package com.chronoio.chrono.presentation.linking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.tools.UrlValidator
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.editor.Editor
import com.chronoio.chrono.presentation.search.ObjectSearchViewModel.VmParams
import javax.inject.Inject

class LinkToObjectViewModelFactory(
    private val vmParams: VmParams,
    private val urlBuilder: UrlBuilder,
    private val searchObjects: SearchObjects,
    private val analytics: Analytics,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val fieldParser: FieldParser,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val spaceViews: SpaceViewSubscriptionContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LinkToObjectViewModel(
            vmParams = vmParams,
            urlBuilder = urlBuilder,
            searchObjects = searchObjects,
            analytics = analytics,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            fieldParser = fieldParser,
            storeOfObjectTypes = storeOfObjectTypes,
            spaceViews = spaceViews
        ) as T
    }
}

class LinkToObjectOrWebViewModelFactory(
    private val vmParams: LinkToObjectOrWebViewModel.VmParams,
    private val urlBuilder: UrlBuilder,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val searchObjects: SearchObjects,
    private val analytics: Analytics,
    private val stores: Editor.Storage,
    private val urlValidator: UrlValidator,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val fieldParser: FieldParser,
    private val spaceViews: SpaceViewSubscriptionContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LinkToObjectOrWebViewModel(
            vmParams = vmParams,
            urlBuilder = urlBuilder,
            storeOfObjectTypes = storeOfObjectTypes,
            searchObjects = searchObjects,
            analytics = analytics,
            stores = stores,
            urlValidator = urlValidator,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            fieldParser = fieldParser,
            spaceViews = spaceViews
        ) as T
    }
}

class BackLinkOrAddToObjectViewModelFactory @Inject constructor(
    private val vmParams: VmParams,
    private val urlBuilder: UrlBuilder,
    private val searchObjects: SearchObjects,
    private val analytics: Analytics,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val fieldParser: FieldParser,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val spaceViews: SpaceViewSubscriptionContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BackLinkOrAddToObjectViewModel(
            vmParams = vmParams,
            urlBuilder = urlBuilder,
            searchObjects = searchObjects,
            analytics = analytics,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            fieldParser = fieldParser,
            storeOfObjectTypes = storeOfObjectTypes,
            spaceViews = spaceViews
        ) as T
    }
}