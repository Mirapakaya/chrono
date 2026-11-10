package com.chronoio.chrono.presentation.multiplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.analytics.base.EventsDictionary
import com.chronoio.chrono.analytics.base.EventsDictionary.clickJoinSpaceWithoutApproval
import com.chronoio.chrono.analytics.base.EventsDictionary.screenInviteRequest
import com.chronoio.chrono.analytics.base.EventsDictionary.screenRequestSent
import com.chronoio.chrono.analytics.base.EventsPropertiesKey
import com.chronoio.chrono.analytics.base.sendEvent
import com.chronoio.chrono.analytics.props.Props
import com.chronoio.chrono.core_models.Notification
import com.chronoio.chrono.core_models.NotificationPayload
import com.chronoio.chrono.core_models.NotificationStatus
import com.chronoio.chrono.core_models.multiplayer.MultiplayerError
import com.chronoio.chrono.core_models.multiplayer.SpaceInviteError
import com.chronoio.chrono.core_models.multiplayer.SpaceInviteView
import com.chronoio.chrono.core_models.multiplayer.SpaceUxType
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.restrictions.SpaceStatus
import com.chronoio.chrono.core_utils.ext.msg
import com.chronoio.chrono.domain.base.fold
import com.chronoio.chrono.domain.base.getOrDefault
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.multiplayer.CheckIsUserSpaceMember
import com.chronoio.chrono.domain.multiplayer.GetSpaceInviteView
import com.chronoio.chrono.domain.multiplayer.SendJoinSpaceRequest
import com.chronoio.chrono.domain.multiplayer.SpaceInviteResolver
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.notifications.SystemNotificationService
import com.chronoio.chrono.domain.spaces.SaveCurrentSpace
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.common.BaseViewModel
import com.chronoio.chrono.presentation.common.TypedViewState
import com.chronoio.chrono.presentation.common.TypedViewState.Error
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RequestJoinSpaceViewModel(
    private val params: Params,
    private val getSpaceInviteView: GetSpaceInviteView,
    private val sendJoinSpaceRequest: SendJoinSpaceRequest,
    private val spaceInviteResolver: SpaceInviteResolver,
    private val checkIsUserSpaceMember: CheckIsUserSpaceMember,
    private val spaceManager: SpaceManager,
    private val saveCurrentSpace: SaveCurrentSpace,
    private val analytics: Analytics,
    private val notificator: SystemNotificationService,
    private val configStorage: ConfigStorage,
    private val spaceViewContainer: SpaceViewSubscriptionContainer
) : BaseViewModel() {

    val state = MutableStateFlow<TypedViewState<SpaceInviteView, ErrorView>>(TypedViewState.Loading)
    val isRequestInProgress = MutableStateFlow(false)
    val showEnableNotificationDialog = MutableStateFlow(false)
    val commands = MutableSharedFlow<Command>(0)
    val showLoadingInviteProgress = MutableStateFlow(false)
    private var getSpaceInviteViewJob: Job? = null
    private var joinSpaceRequestJob: Job? = null

    init {
        Timber.i("RequestJoinSpaceViewModel, init")
        proceedWithGettingSpaceInviteView()
    }

    private fun proceedWithGettingSpaceInviteView() {
        val fileKey = spaceInviteResolver.parseFileKey(params.link)
        val contentId = spaceInviteResolver.parseContentId(params.link)
        if (fileKey != null && contentId != null) {
            showLoadingInviteProgress.value = true
            getSpaceInviteViewJob = viewModelScope.launch {
                getSpaceInviteView.async(
                    GetSpaceInviteView.Params(
                        inviteContentId = contentId,
                        inviteFileKey = fileKey
                    )
                ).fold(
                    onSuccess = { view ->
                        showLoadingInviteProgress.value = false
                        val isAlreadyMember = checkIsUserSpaceMember
                            .async(view.space)
                            .getOrDefault(false)
                        if (isAlreadyMember) {
                            onOpenSpaceClicked(view.space)
                            return@fold
                        } else {
                            val spaceView = spaceViewContainer.get(view.space)
                            if (spaceView != null && spaceView.spaceAccountStatus == SpaceStatus.SPACE_JOINING) {
                                analytics.sendEvent(
                                    eventName = screenInviteRequest,
                                    props = Props(
                                        map = buildMap {
                                            when(spaceView.spaceUxType) {
                                                SpaceUxType.DATA -> put(EventsPropertiesKey.uxType, "Space")
                                                SpaceUxType.CHAT -> put(EventsPropertiesKey.uxType, "Chat")
                                                else -> {}
                                            }
                                            // Analytics Event #5: ScreenInviteRequest with type property
                                            val inviteType = if (view.withoutApprove) {
                                                EventsDictionary.InviteRequestTypes.WITHOUT_APPROVAL
                                            } else {
                                                EventsDictionary.InviteRequestTypes.APPROVAL
                                            }
                                            put(EventsPropertiesKey.type, inviteType)
                                        }
                                    )
                                )
                                state.value = TypedViewState.Error(ErrorView.RequestAlreadySent)
                            } else {
                                // Analytics Event #5: ScreenInviteRequest with type property
                                val inviteType = if (view.withoutApprove) {
                                    EventsDictionary.InviteRequestTypes.WITHOUT_APPROVAL
                                } else {
                                    EventsDictionary.InviteRequestTypes.APPROVAL
                                }
                                analytics.sendEvent(
                                    eventName = screenInviteRequest,
                                    props = Props(
                                        mapOf(EventsPropertiesKey.type to inviteType)
                                    )
                                )
                                state.value = TypedViewState.Success(view)
                            }
                        }
                    },
                    onFailure = { e ->
                        showLoadingInviteProgress.value = false
                        if (e is SpaceInviteError) {
                            when(e) {
                                is SpaceInviteError.InvalidInvite -> {
                                    state.value = Error(
                                        ErrorView.InvalidLink
                                    )
                                }
                                is SpaceInviteError.InvalidNotFound -> {
                                    state.value = Error(
                                        ErrorView.InviteNotFound
                                    )
                                }
                                is SpaceInviteError.SpaceDeleted -> {
                                    state.value = Error(
                                        ErrorView.SpaceDeleted
                                    )
                                }
                                SpaceInviteError.InviteNotActive -> {
                                    state.value = Error(
                                        ErrorView.InvalidLink
                                    )
                                }
                            }
                        }
                        Timber.e(e, "Error while getting space invite view")
                    }
                )
            }
        } else {
            Timber.w("Could not parse invite link: ${params.link}")
            state.value = TypedViewState.Error(ErrorView.InvalidLink)
        }
    }

    fun onCancelLoadingInviteClicked() {
        getSpaceInviteViewJob?.cancel()
        showLoadingInviteProgress.value = false
    }

    fun onRequestToJoinClicked() {
        val currentState = state.value
        if (currentState !is TypedViewState.Success) return

        joinSpaceRequestJob?.cancel()
        joinSpaceRequestJob = viewModelScope.launch {
            val fileKey = spaceInviteResolver.parseFileKey(params.link)
            val contentId = spaceInviteResolver.parseContentId(params.link)

            if (fileKey == null || contentId == null) {
                Timber.w("Could not parse invite link in onRequestToJoinClicked: ${params.link}")
                return@launch
            }

            isRequestInProgress.value = true

            val params = SendJoinSpaceRequest.Params(
                space = currentState.data.space,
                network = configStorage.getOrNull()?.network,
                inviteFileKey = fileKey,
                inviteContentId = contentId
            )

            sendJoinSpaceRequest.async(params).fold(
                onFailure = { handleJoinRequestFailure(it) },
                onSuccess = { handleJoinRequestSuccess(currentState.data) }
            )

            isRequestInProgress.value = false
        }
    }

    private suspend fun handleJoinRequestFailure(error: Throwable) {
        Timber.e(error, "Error while sending space join request")
        when (error) {
            is MultiplayerError.Generic -> commands.emit(Command.ShowGenericMultiplayerError(error))
            else -> sendToast(error.msg())
        }
    }

    private suspend fun handleJoinRequestSuccess(data: SpaceInviteView) {

        val spaceView = spaceViewContainer.get(data.space)
        val spaceUxType = spaceView?.spaceUxType

        analytics.sendEvent(
            eventName = screenRequestSent,
            props = Props(
                map = when(spaceUxType) {
                    SpaceUxType.DATA -> mapOf(
                        EventsPropertiesKey.uxType to "Space"
                    )
                    SpaceUxType.CHAT -> mapOf(
                        EventsPropertiesKey.uxType to "Chat"
                    )
                    else -> emptyMap()
                }
            )
        )

        if (data.withoutApprove) {
            analytics.sendEvent(eventName = clickJoinSpaceWithoutApproval)
        }

        val shouldNotify = data.withoutApprove
        val notificationsEnabled = notificator.areNotificationsEnabled

        if (shouldNotify) {
            sendApprovalNotification(data)
        }

        if (notificationsEnabled) {
            if (!shouldNotify) {
                commands.emit(Command.Toast.RequestSent)
            }
            commands.emit(Command.Dismiss)
        } else {
            if (!shouldNotify) {
                commands.emit(Command.Toast.RequestSent)
            }
            showEnableNotificationDialog.value = true
        }
    }

    private fun createApprovalNotification(data: SpaceInviteView): Notification {
        return Notification(
            id = Random.nextInt().toString(),
            createTime = System.currentTimeMillis(),
            status = NotificationStatus.CREATED,
            isLocal = true,
            payload = NotificationPayload.ParticipantWithoutApprovalRequestApproved(
                spaceId = data.space,
                spaceName = data.spaceName
            ),
            space = data.space
        )
    }

    private fun sendApprovalNotification(data: SpaceInviteView) {
        notificator.notify(createApprovalNotification(data))
    }

    fun onCancelJoinSpaceRequestClicked() {
        joinSpaceRequestJob?.cancel()
        isRequestInProgress.value = false
    }

    fun onOpenSpaceClicked(space: SpaceId) {
        viewModelScope.launch {
            val curr = spaceManager.get()
            if (curr == space.id) {
                commands.emit(Command.Dismiss)
            } else {
                spaceManager.set(space.id)
                saveCurrentSpace.async(params = SaveCurrentSpace.Params(space)).fold(
                    onSuccess = {
                        Timber.i("Current space saved successfully after joining space")
                    },
                    onFailure = { error ->
                        Timber.e(error, "Error while saving current space after joining")
                    }
                )
                commands.emit(Command.SwitchToSpace(space))
            }
        }
    }

    fun onNotificationPromptDismissed() {
        viewModelScope.launch {
            commands.emit(Command.Dismiss)
        }
    }

    class Factory @Inject constructor(
        private val params: Params,
        private val getSpaceInviteView: GetSpaceInviteView,
        private val sendJoinSpaceRequest: SendJoinSpaceRequest,
        private val spaceInviteResolver: SpaceInviteResolver,
        private val checkIsUserSpaceMember: CheckIsUserSpaceMember,
        private val saveCurrentSpace: SaveCurrentSpace,
        private val spaceManager: SpaceManager,
        private val analytics: Analytics,
        private val notificator: SystemNotificationService,
        private val configStorage: ConfigStorage,
        private val spaceViewContainer: SpaceViewSubscriptionContainer
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = RequestJoinSpaceViewModel(
            params = params,
            getSpaceInviteView = getSpaceInviteView,
            sendJoinSpaceRequest = sendJoinSpaceRequest,
            spaceInviteResolver = spaceInviteResolver,
            checkIsUserSpaceMember = checkIsUserSpaceMember,
            saveCurrentSpace = saveCurrentSpace,
            spaceManager = spaceManager,
            analytics = analytics,
            notificator = notificator,
            configStorage = configStorage,
            spaceViewContainer = spaceViewContainer
        ) as T
    }

    data class Params(val link: String)

    sealed class Command {
        sealed class Toast : Command() {
            data object RequestSent : Toast()
            data object SpaceNotFound : Toast()
            data object SpaceDeleted : Toast()
        }
        data class ShowGenericMultiplayerError(val error: MultiplayerError.Generic) : Command()
        data object Dismiss: Command()
        data class SwitchToSpace(val space: SpaceId): Command()
    }

    sealed class ErrorView {
        data object InvalidLink : ErrorView()
        data object InviteNotFound : ErrorView()
        data object SpaceDeleted : ErrorView()
        data object RequestAlreadySent: ErrorView()
     }
}