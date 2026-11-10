package com.chronoio.chrono.data.auth.event

import com.chronoio.chrono.core_models.FileLimitsEvent
import com.chronoio.chrono.domain.workspace.FileLimitsEventChannel
import kotlinx.coroutines.flow.Flow

class FileLimitsDataChannel(
    private val channel: FileLimitsRemoteChannel
) : FileLimitsEventChannel {

    override fun observe(): Flow<List<FileLimitsEvent>> {
        return channel.observe()
    }
}