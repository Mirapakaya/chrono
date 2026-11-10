package com.chronoio.chrono.feature_vault.presentation

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.ObjectWrapper
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
import com.chronoio.chrono.domain.spaces.SetHomepage
import com.chronoio.chrono.domain.spaces.SaveCurrentSpace
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

object VaultViewModelFabric {
    fun create(
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer = mock(),
        urlBuilder: UrlBuilder = mock(),
        spaceManager: SpaceManager = mock(),
        saveCurrentSpace: SaveCurrentSpace = mock(),
        analytics: Analytics = mock(),
        deepLinkToObjectDelegate: DeepLinkToObjectDelegate = mock(),
        appActionManager: AppActionManager = mock(),
        spaceInviteResolver: SpaceInviteResolver = mock(),
        profileContainer: ProfileSubscriptionManager = mock {
            on { observe() }.thenReturn(flowOf(ObjectWrapper.Basic(emptyMap())))
        },
        chatPreviewContainer: ChatPreviewContainer = mock(),
        chatsDetailsContainer: ChatsDetailsSubscriptionContainer = mock {
            on { observe() }.thenReturn(flowOf(emptyList()))
        },
        pendingIntentStore: PendingIntentStore = mock(),
        stringResourceProvider: StringResourceProvider = mock(),
        dateProvider: DateProvider = mock(),
        fieldParser: FieldParser = mock(),
        storeOfObjectTypes: StoreOfObjectTypes = mock(),
        setSpaceNotificationMode: SetSpaceNotificationMode = mock(),
        deleteSpace: DeleteSpace = mock(),
        userPermissionProvider: UserPermissionProvider = mock(),
        notificationPermissionManager: NotificationPermissionManager = mock(),
        unpinSpace: UnpinSpace = mock(),
        setSpaceOrder: SetSpaceOrder = mock(),
        getSpaceWallpaper: GetSpaceWallpapers = mock(),
        searchOneToOneChatByIdentity: SearchOneToOneChatByIdentity = mock(),
        shouldShowCreateSpaceBadge: ShouldShowCreateSpaceBadge = mock {
            on { runBlocking { async(any()) } }.thenReturn(com.chronoio.chrono.domain.base.Resultat.Success(false))
        },
        setCreateSpaceBadgeSeen: SetCreateSpaceBadgeSeen = mock(),
        setVaultSearchHighlightSeen: SetVaultSearchHighlightSeen = mock(),
        appInfo: AppInfo = mock {
            on { versionName }.thenReturn("1.0.0-test")
        },
        participantSubscriptionContainer: ParticipantSubscriptionContainer = mock(),
        configStorage: ConfigStorage = mock {
            on { getOrNull() }.thenReturn(null)
            on { getAccountId() }.thenReturn(null)
        },
        createSpace: CreateSpace = mock(),
        deepLinkResolver: DeepLinkResolver = mock(),
        osWidgetSpacesSync: OsWidgetSpacesSync = mock(),
        osWidgetDataViewSync: OsWidgetDataViewSync = mock(),
        networkModeProvider: NetworkModeProvider = mock(),
        getMembershipFeatures: GetMembershipFeatures = mock(),
        resolveSpaceHomepage: ResolveSpaceHomepage = mock(),
        setHomepage: SetHomepage = mock(),
        userSettingsRepository: UserSettingsRepository = mock {
            on { observeCompactModeEnabled() }.thenReturn(flowOf(false))
            on { observeQuickCaptureEnabled() }.thenReturn(flowOf(false))
            onBlocking { getVaultSortKeys() }.thenReturn(emptyMap())
        }
    ): VaultViewModel = VaultViewModel(
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
        getSpaceWallpapers = getSpaceWallpaper,
        shouldShowCreateSpaceBadge = shouldShowCreateSpaceBadge,
        setCreateSpaceBadgeSeen = setCreateSpaceBadgeSeen,
        setVaultSearchHighlightSeen = setVaultSearchHighlightSeen,
        appInfo = appInfo,
        participantContainer = participantSubscriptionContainer,
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
    )
}
