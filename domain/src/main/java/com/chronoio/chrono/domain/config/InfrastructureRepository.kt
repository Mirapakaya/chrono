package com.chronoio.chrono.domain.config

interface InfrastructureRepository {
    suspend fun enableAnytypeContextMenu()
    suspend fun disableAnytypeContextMenu()
    suspend fun getAnytypeContextMenu(): Boolean
}