package com.chronoio.chrono.domain.theme

import com.chronoio.chrono.core_models.ThemeMode
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.config.UserSettingsRepository

class SetTheme(
    private val repo: UserSettingsRepository
) : BaseUseCase<Unit, ThemeMode>() {
    override suspend fun run(params: ThemeMode) = safe {
        repo.setThemeMode(params)
    }
}