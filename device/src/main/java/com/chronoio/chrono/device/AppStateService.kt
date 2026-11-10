package com.chronoio.chrono.device

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.chronoio.chrono.core_models.AppState
import com.chronoio.chrono.domain.base.fold
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.device.SetAppState
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Singleton
class AppStateService @Inject constructor(
    private val setAppState: SetAppState,
    private val coroutineScope: CoroutineScope,
    private val settings: UserSettingsRepository,
    private val clock: () -> Long = { System.currentTimeMillis() / 1000 }
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        handleStateTransition(AppState.FOREGROUND)
    }

    override fun onStop(owner: LifecycleOwner) {
        handleStateTransition(AppState.BACKGROUND)
        stampBackgroundedAt()
    }

    /**
     * Records when the app went to background so [LaunchAccount] can expire the
     * "restore into the last opened space" route on the next cold start
     * (DROID-4590). Best effort: a crash or an OS kill skips this, leaving the
     * previous stamp, which errs toward expiring — the safe direction.
     */
    private fun stampBackgroundedAt() {
        coroutineScope.launch {
            runCatching { settings.setLastBackgroundedAt(clock()) }
                .onFailure { Timber.w(it, "Failed to stamp backgrounded-at") }
        }
    }

    private fun handleStateTransition(state: AppState) {
        coroutineScope.launch {
            val params = SetAppState.Params(state)
            setAppState.async(params).fold(
                onSuccess = {
                    Timber.d("App state set successfully: $state")
                },
                onFailure = { error ->
                    Timber.d("Failed to set app state: $error")
                }
            )
        }
    }
} 