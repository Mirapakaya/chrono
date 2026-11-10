package com.chronoio.chrono.domain.theme

import com.chronoio.chrono.core_models.ThemeMode
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.config.UserSettingsRepository

class GetTheme(
    private val repo: UserSettingsRepository
) : BaseUseCase<ThemeMode, BaseUseCase.None>() {
    override suspend fun run(params: None) = safe {
        repo.getThemeMode()
    }
}