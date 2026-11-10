package com.chronoio.chrono.domain.event.interactor

import com.chronoio.chrono.core_models.multiplayer.SpaceSyncAndP2PStatusState
import kotlinx.coroutines.flow.Flow

interface SpaceSyncAndP2PStatusProvider {
    fun observe(): Flow<SpaceSyncAndP2PStatusState>
}