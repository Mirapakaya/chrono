package com.chronoio.chrono.domain.chats

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.chats.Chat

/** The message orders and chat state rendered together by the UI. */
@ConsistentCopyVisibility
data class ChatReadSnapshot internal constructor(
    internal val owner: Any,
    internal val orders: Map<Id, Id>,
    val state: Chat.State
)
