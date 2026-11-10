package com.chronoio.chrono.data.auth.repo

import com.chronoio.chrono.domain.config.InfrastructureRepository

class InfrastructureDataRepository(
    private val cache: DebugSettingsCache
) : InfrastructureRepository {

    override suspend fun enableAnytypeContextMenu() {
        cache.enableAnytypeContextMenu()
    }

    override suspend fun disableAnytypeContextMenu() {
        cache.disableAnytypeContextMenu()
    }

    override suspend fun getAnytypeContextMenu(): Boolean = cache.getAnytypeContextMenu()
}