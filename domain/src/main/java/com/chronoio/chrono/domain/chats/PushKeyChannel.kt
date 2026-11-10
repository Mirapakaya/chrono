package com.chronoio.chrono.domain.chats

import com.chronoio.chrono.core_models.chats.PushKeyUpdate
import kotlinx.coroutines.flow.Flow

interface PushKeyChannel {
    fun observe(): Flow<PushKeyUpdate>
}