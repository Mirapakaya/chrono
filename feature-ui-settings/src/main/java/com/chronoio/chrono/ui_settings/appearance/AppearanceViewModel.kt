package com.chronoio.chrono.ui_settings.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.ThemeMode
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.theme.GetTheme
import com.chronoio.chrono.domain.theme.SetTheme
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.extension.sendChangeThemeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AppearanceViewModel(
    private val getTheme: GetTheme,
    private val setTheme: SetTheme,
    private val themeApplicator: ThemeApplicator,
    private val analytics: Analytics,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
) : ViewModel(), AnalyticSpaceHelperDelegate by analyticSpaceHelperDelegate {

    val selectedTheme = MutableStateFlow<ThemeMode>(ThemeMode.System)

    init {
        viewModelScope.launch {
            getTheme(BaseUseCase.None).proceed(
                success = {
                    proceedWithUpdatingSelectedTheme(it)
                },
                failure = {
                    Timber.e(it, "Error while getting current app theme")
                })
        }
    }

    private fun saveTheme(mode: ThemeMode) {
        viewModelScope.launch {
            analytics.sendChangeThemeEvent(mode)
        }
        viewModelScope.launch {
            setTheme(params = mode).proceed(
                success = {
                    proceedWithUpdatingTheme(mode)
                },
                failure = {
                    Timber.e(it, "Error while setting current app theme")
                }
            )
        }
    }

    fun onLight() {
        saveTheme(ThemeMode.Light)
    }

    fun onDark() {
        saveTheme(ThemeMode.Night)
    }

    fun onSystem() {
        saveTheme(ThemeMode.System)
    }

    private fun proceedWithUpdatingTheme(themeMode: ThemeMode) {
        themeApplicator.apply(themeMode)
        proceedWithUpdatingSelectedTheme(themeMode)
    }

    private fun proceedWithUpdatingSelectedTheme(themeMode: ThemeMode) {
        selectedTheme.value = themeMode
    }

    class Factory @Inject constructor(
        private val getTheme: GetTheme,
        private val setTheme: SetTheme,
        private val themeApplicator: ThemeApplicator,
        private val analytics: Analytics,
        private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AppearanceViewModel(
                getTheme = getTheme,
                setTheme = setTheme,
                themeApplicator = themeApplicator,
                analytics = analytics,
                analyticSpaceHelperDelegate = analyticSpaceHelperDelegate
            ) as T
        }
    }
}