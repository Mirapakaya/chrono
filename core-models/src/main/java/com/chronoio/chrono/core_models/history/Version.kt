package com.chronoio.chrono.core_models.history

import com.chronoio.chrono.core_models.Event
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectView
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.primitives.TimestampInSeconds

data class Version(
    val id: Id,
    val previousIds: List<Id>,
    val spaceMember: Id,
    val spaceMemberName: String,
    val timestamp: TimestampInSeconds,
    val groupId: Long
)

data class ShowVersionResponse(
    val payload: Payload?,
    val version: Version?,
    val traceId: Id
)

data class DiffVersionResponse(
    val historyEvents: List<Event.Command>,
    val objectView: ObjectView?
)
