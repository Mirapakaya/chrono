package com.chronoio.chrono.feature_vault.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.notifications.NotificationPermissionManager
import com.chronoio.chrono.core_utils.tools.AppInfo
import com.chronoio.chrono.domain.chats.ChatPreviewContainer
import com.chronoio.chrono.domain.chats.ChatsDetailsSubscriptionContainer
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.deeplink.PendingIntentStore
import com.chronoio.chrono.domain.misc.AppActionManager
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.domain.multiplayer.ParticipantSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.SearchOneToOneChatByIdentity
import com.chronoio.chrono.domain.multiplayer.SpaceInviteResolver
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.notifications.SetSpaceNotificationMode
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.ProfileSubscriptionManager
import com.chronoio.chrono.domain.spaces.CreateSpace
import com.chronoio.chrono.domain.spaces.DeleteSpace
import com.chronoio.chrono.domain.spaces.ResolveSpaceHomepage
import com.chronoio.chrono.domain.spaces.SaveCurrentSpace
import com.chronoio.chrono.domain.spaces.SetHomepage
import com.chronoio.chrono.domain.vault.SetCreateSpaceBadgeSeen
import com.chronoio.chrono.domain.vault.SetSpaceOrder
import com.chronoio.chrono.domain.vault.SetVaultSearchHighlightSeen
import com.chronoio.chrono.domain.vault.ShouldShowCreateSpaceBadge
import com.chronoio.chrono.domain.vault.UnpinSpace
import com.chronoio.chrono.domain.wallpaper.GetSpaceWallpapers
import com.chronoio.chrono.domain.network.NetworkModeProvider
import com.chronoio.chrono.domain.payments.GetMembershipFeatures
import com.chronoio.chrono.domain.widgets.OsWidgetDataViewSync
import com.chronoio.chrono.domain.widgets.OsWidgetSpacesSync
import com.chronoio.chrono.domain.workspace.DeepLinkToObjectDelegate
import com.chronoio.chrono.domain.workspace.SpaceManager
import javax.inject.Inject

class VaultViewModelFactory @Inject constructor(
    private val spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
    private val urlBuilder: UrlBuilder,
    private val spaceManager: SpaceManager,
    private val saveCurrentSpace: SaveCurrentSpace,
    private val analytics: Analytics,
    private val deepLinkToObjectDelegate: DeepLinkToObjectDelegate,
    private val appActionManager: AppActionManager,
    private val spaceInviteResolver: SpaceInviteResolver,
    private val profileContainer: ProfileSubscriptionManager,
    private val chatPreviewContainer: ChatPreviewContainer,
    private val chatsDetailsContainer: ChatsDetailsSubscriptionContainer,
    private val participantContainer: ParticipantSubscriptionContainer,
    private val pendingIntentStore: PendingIntentStore,
    private val stringResourceProvider: StringResourceProvider,
    private val dateProvider: DateProvider,
    private val fieldParser: FieldParser,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val setSpaceNotificationMode: SetSpaceNotificationMode,
    private val deleteSpace: DeleteSpace,
    private val userPermissionProvider: UserPermissionProvider,
    private val notificationPermissionManager: NotificationPermissionManager,
    private val unpinSpace: UnpinSpace,
    private val setSpaceOrder: SetSpaceOrder,
    private val getSpaceWallpapers: GetSpaceWallpapers,
    private val shouldShowCreateSpaceBadge: ShouldShowCreateSpaceBadge,
    private val setCreateSpaceBadgeSeen: SetCreateSpaceBadgeSeen,
    private val setVaultSearchHighlightSeen: SetVaultSearchHighlightSeen,
    private val appInfo: AppInfo,
    private val searchOneToOneChatByIdentity: SearchOneToOneChatByIdentity,
    private val createSpace: CreateSpace,
    private val deepLinkResolver: DeepLinkResolver,
    private val configStorage: ConfigStorage,
    private val osWidgetSpacesSync: OsWidgetSpacesSync,
    private val osWidgetDataViewSync: OsWidgetDataViewSync,
    private val networkModeProvider: NetworkModeProvider,
    private val getMembershipFeatures: GetMembershipFeatures,
    private val resolveSpaceHomepage: ResolveSpaceHomepage,
    private val setHomepage: SetHomepage,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ) = VaultViewModel(
        spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
        urlBuilder = urlBuilder,
        spaceManager = spaceManager,
        saveCurrentSpace = saveCurrentSpace,
        analytics = analytics,
        deepLinkToObjectDelegate = deepLinkToObjectDelegate,
        appActionManager = appActionManager,
        spaceInviteResolver = spaceInviteResolver,
        profileContainer = profileContainer,
        chatPreviewContainer = chatPreviewContainer,
        chatsDetailsContainer = chatsDetailsContainer,
        participantContainer = participantContainer,
        pendingIntentStore = pendingIntentStore,
        stringResourceProvider = stringResourceProvider,
        dateProvider = dateProvider,
        fieldParser = fieldParser,
        storeOfObjectTypes = storeOfObjectTypes,
        setSpaceNotificationMode = setSpaceNotificationMode,
        deleteSpace = deleteSpace,
        userPermissionProvider = userPermissionProvider,
        notificationPermissionManager = notificationPermissionManager,
        unpinSpace = unpinSpace,
        setSpaceOrder = setSpaceOrder,
        getSpaceWallpapers = getSpaceWallpapers,
        shouldShowCreateSpaceBadge = shouldShowCreateSpaceBadge,
        setCreateSpaceBadgeSeen = setCreateSpaceBadgeSeen,
        setVaultSearchHighlightSeen = setVaultSearchHighlightSeen,
        appInfo = appInfo,
        searchOneToOneChatByIdentity = searchOneToOneChatByIdentity,
        createSpace = createSpace,
        deepLinkResolver = deepLinkResolver,
        configStorage = configStorage,
        osWidgetSpacesSync = osWidgetSpacesSync,
        osWidgetDataViewSync = osWidgetDataViewSync,
        networkModeProvider = networkModeProvider,
        getMembershipFeatures = getMembershipFeatures,
        resolveSpaceHomepage = resolveSpaceHomepage,
        setHomepage = setHomepage,
        userSettingsRepository = userSettingsRepository
    ) as T
}
