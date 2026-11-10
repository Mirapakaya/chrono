package com.chronoio.chrono.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.CrashReporter
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.domain.auth.interactor.CheckAuthorizationStatus
import com.chronoio.chrono.domain.auth.interactor.GetLastOpenedObject
import com.chronoio.chrono.domain.auth.interactor.LaunchAccount
import com.chronoio.chrono.domain.auth.interactor.LaunchWallet
import com.chronoio.chrono.domain.deeplink.PendingIntentStore
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.page.CreateObjectByTypeAndTemplate
import com.chronoio.chrono.domain.spaces.GetLastOpenedSpace
import com.chronoio.chrono.domain.subscriptions.GlobalSubscriptionManager
import com.chronoio.chrono.domain.vault.SetVaultSearchHighlightSeen
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.auth.account.MigrationHelperDelegate
import javax.inject.Inject

/**
 * Created by Konstantin Ivanov
 * email :  ki@agileburo.com
 * on 2019-10-21.
 */
class SplashViewModelFactory @Inject constructor(
    private val checkAuthorizationStatus: CheckAuthorizationStatus,
    private val launchAccount: LaunchAccount,
    private val launchWallet: LaunchWallet,
    private val analytics: Analytics,
    private val getLastOpenedObject: GetLastOpenedObject,
    private val crashReporter: CrashReporter,
    private val localeProvider: LocaleProvider,
    private val spaceManager: SpaceManager,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val globalSubscriptionManager: GlobalSubscriptionManager,
    private val getLastOpenedSpace: GetLastOpenedSpace,
    private val createObjectByTypeAndTemplate: CreateObjectByTypeAndTemplate,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val migration: MigrationHelperDelegate,
    private val deepLinkResolver: DeepLinkResolver,
    private val pendingIntentStore: PendingIntentStore,
    private val searchObjects: SearchObjects,
    private val preferredSpaceIdHolder: PreferredSpaceIdHolder,
    private val setVaultSearchHighlightSeen: SetVaultSearchHighlightSeen
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SplashViewModel(
            checkAuthorizationStatus = checkAuthorizationStatus,
            launchAccount = launchAccount,
            launchWallet = launchWallet,
            analytics = analytics,
            getLastOpenedObject = getLastOpenedObject,
            crashReporter = crashReporter,
            localeProvider = localeProvider,
            spaceManager = spaceManager,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            globalSubscriptionManager = globalSubscriptionManager,
            getLastOpenedSpace = getLastOpenedSpace,
            createObjectByTypeAndTemplate = createObjectByTypeAndTemplate,
            spaceViews = spaceViews,
            migration = migration,
            deepLinkResolver = deepLinkResolver,
            pendingIntentStore = pendingIntentStore,
            searchObjects = searchObjects,
            preferredSpaceIdHolder = preferredSpaceIdHolder,
            setVaultSearchHighlightSeen = setVaultSearchHighlightSeen
        ) as T
}
