package com.chronoio.chrono.feature_chats.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.notifications.NotificationPermissionManager
import com.chronoio.chrono.domain.auth.interactor.GetAccount
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.chats.AddChatMessage
import com.chronoio.chrono.domain.chats.ChatContainer
import com.chronoio.chrono.domain.chats.DeleteChatMessage
import com.chronoio.chrono.domain.chats.EditChatMessage
import com.chronoio.chrono.domain.chats.ToggleChatMessageReaction
import com.chronoio.chrono.domain.invite.GetCurrentInviteAccessLevel
import com.chronoio.chrono.domain.invite.SpaceInviteLinkStore
import com.chronoio.chrono.domain.media.DiscardPreloadedFile
import com.chronoio.chrono.domain.media.PreloadFile
import com.chronoio.chrono.domain.media.UploadFile
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.domain.misc.GetLinkPreview
import com.chronoio.chrono.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.notifications.NotificationBuilder
import com.chronoio.chrono.domain.notifications.SetChatNotificationMode
import com.chronoio.chrono.domain.`object`.GetObject
import com.chronoio.chrono.domain.`object`.SetObjectDetails
import com.chronoio.chrono.domain.objects.CreateObjectFromUrl
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.spaces.SetSpaceDetails
import com.chronoio.chrono.feature_chats.tools.ClearChatsTempFolder
import com.chronoio.chrono.presentation.util.CopyFileToCacheDirectory
import com.chronoio.chrono.presentation.navigation.backstack.BackHistoryDelegate
import com.chronoio.chrono.presentation.vault.ExitToVaultDelegate
import com.chronoio.chrono.presentation.widgets.PinObjectAsWidgetDelegate
import javax.inject.Inject

class ChatViewModelFactory @Inject constructor(
    private val params: ChatViewModel.Params.Default,
    private val chatContainer: ChatContainer,
    private val addChatMessage: AddChatMessage,
    private val editChatMessage: EditChatMessage,
    private val deleteChatMessage: DeleteChatMessage,
    private val toggleChatMessageReaction: ToggleChatMessageReaction,
    private val members: ActiveSpaceMemberSubscriptionContainer,
    private val getAccount: GetAccount,
    private val urlBuilder: UrlBuilder,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val dispatchers: AppCoroutineDispatchers,
    private val uploadFile: UploadFile,
    private val preloadFile: PreloadFile,
    private val discardPreloadedFile: DiscardPreloadedFile,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val copyFileToCacheDirectory: CopyFileToCacheDirectory,
    private val exitToVaultDelegate: ExitToVaultDelegate,
    private val getLinkPreview: GetLinkPreview,
    private val createObjectFromUrl: CreateObjectFromUrl,
    private val notificationPermissionManager: NotificationPermissionManager,
    private val spacePermissionProvider: UserPermissionProvider,
    private val notificationBuilder: NotificationBuilder,
    private val clearChatsTempFolder: ClearChatsTempFolder,
    private val createObject: CreateObject,
    private val getObject: GetObject,
    private val analytics: Analytics,
    private val spaceInviteLinkStore: SpaceInviteLinkStore,
    private val getCurrentInviteAccessLevel: GetCurrentInviteAccessLevel,
    private val pinObjectAsWidgetDelegate: PinObjectAsWidgetDelegate,
    private val setObjectListIsArchived: SetObjectListIsArchived,
    private val setObjectDetails: SetObjectDetails,
    private val setSpaceDetails: SetSpaceDetails,
    private val setChatNotificationMode: SetChatNotificationMode,
    private val fieldParser: FieldParser,
    private val deepLinkResolver: DeepLinkResolver,
    private val backHistoryDelegate: BackHistoryDelegate
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ChatViewModel(
        vmParams = params,
        chatContainer = chatContainer,
        addChatMessage = addChatMessage,
        editChatMessage = editChatMessage,
        deleteChatMessage = deleteChatMessage,
        toggleChatMessageReaction = toggleChatMessageReaction,
        members = members,
        getAccount = getAccount,
        urlBuilder = urlBuilder,
        spaceViews = spaceViews,
        dispatchers = dispatchers,
        uploadFile = uploadFile,
        storeOfObjectTypes = storeOfObjectTypes,
        copyFileToCacheDirectory = copyFileToCacheDirectory,
        exitToVaultDelegate = exitToVaultDelegate,
        getLinkPreview = getLinkPreview,
        createObjectFromUrl = createObjectFromUrl,
        notificationPermissionManager = notificationPermissionManager,
        spacePermissionProvider = spacePermissionProvider,
        notificationBuilder = notificationBuilder,
        clearChatsTempFolder = clearChatsTempFolder,
        createObject = createObject,
        getObject = getObject,
        analytics = analytics,
        preloadFile = preloadFile,
        discardPreloadedFile = discardPreloadedFile,
        spaceInviteLinkStore = spaceInviteLinkStore,
        getCurrentInviteAccessLevel = getCurrentInviteAccessLevel,
        pinObjectAsWidgetDelegate = pinObjectAsWidgetDelegate,
        setObjectListIsArchived = setObjectListIsArchived,
        setObjectDetails = setObjectDetails,
        setSpaceDetails = setSpaceDetails,
        setChatNotificationMode = setChatNotificationMode,
        fieldParser = fieldParser,
        deepLinkResolver = deepLinkResolver,
        backHistoryDelegate = backHistoryDelegate
    ) as T
}
