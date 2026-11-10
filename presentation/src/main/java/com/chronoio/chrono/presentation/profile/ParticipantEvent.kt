package com.chronoio.chrono.presentation.profile

sealed class ParticipantEvent{
    data object OnDismiss: ParticipantEvent()
    data object OnCardClicked: ParticipantEvent()
    data object OnConnectClicked: ParticipantEvent()
    data object OnRemoveMemberClicked: ParticipantEvent()
}