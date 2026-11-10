package com.chronoio.chrono.data.auth.repo

interface DebugSettingsCache {
    suspend fun enableAnytypeContextMenu()
    suspend fun disableAnytypeContextMenu()
    suspend fun getAnytypeContextMenu(): Boolean
}