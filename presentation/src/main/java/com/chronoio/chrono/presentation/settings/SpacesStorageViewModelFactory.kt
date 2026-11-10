package com.chronoio.chrono.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.domain.auth.interactor.GetAccount
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.workspace.InterceptFileLimitEvents
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.domain.workspace.SpacesUsageInfo
import com.chronoio.chrono.presentation.membership.provider.MembershipProvider
import javax.inject.Inject

class SpacesStorageViewModelFactory @Inject constructor(
    private val vmParams: SpacesStorageViewModel.VmParams,
    private val analytics: Analytics,
    private val spaceManager: SpaceManager,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val spacesUsageInfo: SpacesUsageInfo,
    private val interceptFileLimitEvents: InterceptFileLimitEvents,
    private val storelessSubscriptionContainer: StorelessSubscriptionContainer,
    private val getAccount: GetAccount,
    private val membershipProvider: MembershipProvider,
    private val userPermissionProvider: UserPermissionProvider,
    private val configStorage: ConfigStorage
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T = SpacesStorageViewModel(
        analytics = analytics,
        spaceManager = spaceManager,
        appCoroutineDispatchers = appCoroutineDispatchers,
        spacesUsageInfo = spacesUsageInfo,
        interceptFileLimitEvents = interceptFileLimitEvents,
        storelessSubscriptionContainer = storelessSubscriptionContainer,
        getAccount = getAccount,
        membershipProvider = membershipProvider,
        userPermissionProvider = userPermissionProvider,
        configStorage = configStorage,
        vmParams = vmParams
    ) as T
}