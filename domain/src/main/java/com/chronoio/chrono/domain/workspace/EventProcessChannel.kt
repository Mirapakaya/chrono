package com.chronoio.chrono.domain.workspace

import com.chronoio.chrono.core_models.Process
import kotlinx.coroutines.flow.Flow

interface EventProcessImportChannel {
    fun observe(): Flow<List<Process.Event.Import>>
}

interface EventProcessDropFilesChannel {
    fun observe(): Flow<List<Process.Event.DropFiles>>
}

interface EventProcessMigrationChannel {
    fun observe(): Flow<List<Process.Event.Migration>>
}