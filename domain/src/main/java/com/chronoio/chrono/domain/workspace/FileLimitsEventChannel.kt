package com.chronoio.chrono.domain.workspace

import com.chronoio.chrono.core_models.FileLimitsEvent
import kotlinx.coroutines.flow.Flow

interface FileLimitsEventChannel {
    fun observe() : Flow<List<FileLimitsEvent>>
}