package com.chronoio.chrono.data.auth.event

import com.chronoio.chrono.core_models.FileLimitsEvent
import kotlinx.coroutines.flow.Flow

interface FileLimitsRemoteChannel {
    fun observe(): Flow<List<FileLimitsEvent>>
}