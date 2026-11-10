package com.chronoio.chrono.domain.vault

import com.chronoio.chrono.core_models.settings.VaultSettings
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class GetVaultSettings @Inject constructor(
    private val settings: UserSettingsRepository,
    private val auth: AuthRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<Unit, VaultSettings>(dispatchers.io) {

    override suspend fun doWork(params: Unit): VaultSettings {
        val acc = auth.getCurrentAccount()
        return settings.getVaultSettings(acc)
    }
}