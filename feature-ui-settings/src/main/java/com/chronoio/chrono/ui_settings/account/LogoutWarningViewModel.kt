package com.chronoio.chrono.ui_settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.analytics.base.EventsDictionary
import com.chronoio.chrono.analytics.base.EventsPropertiesKey
import com.chronoio.chrono.analytics.base.sendEvent
import com.chronoio.chrono.analytics.props.Props
import com.chronoio.chrono.domain.auth.interactor.Logout
import com.chronoio.chrono.domain.base.Interactor
import com.chronoio.chrono.domain.misc.AppActionManager
import com.chronoio.chrono.domain.subscriptions.GlobalSubscriptionManager
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class LogoutWarningViewModel(
    private val logout: Logout,
    private val analytics: Analytics,
    private val appActionManager: AppActionManager,
    private val globalSubscriptionManager: GlobalSubscriptionManager
) : ViewModel() {

    val commands = MutableSharedFlow<Command>(replay = 0)
    val isLoggingOut = MutableStateFlow(false)

    fun onLogoutClicked() {
        val startTime = System.currentTimeMillis()
        viewModelScope.launch {
            logout(
                params = Logout.Params(clearLocalRepositoryData = false)
            ).collect { status ->
                when (status) {
                    is Interactor.Status.Started -> {
                        isLoggingOut.value = true
                    }
                    is Interactor.Status.Success -> {
                        viewModelScope.sendEvent(
                            analytics = analytics,
                            startTime = startTime,
                            middleTime = System.currentTimeMillis(),
                            eventName = EventsDictionary.logout,
                            props = Props(
                                mapOf(
                                    EventsPropertiesKey.route to EventsDictionary.Routes.screenSettings
                                )
                            )
                        )
                        appActionManager.setup(AppActionManager.Action.ClearAll)
                        unsubscribeFromGlobalSubscriptions()
                        isLoggingOut.value = false
                        commands.emit(Command.Logout)
                    }
                    is Interactor.Status.Error -> {
                        isLoggingOut.value = false
                        commands.emit(Command.ShowError(status.throwable.message ?: ""))
                        Timber.e(status.throwable.message, "Error while logging out")
                    }
                }
            }
        }
    }

    private fun unsubscribeFromGlobalSubscriptions() {
        globalSubscriptionManager.onStop()
    }

    fun onBackupClicked() {
        viewModelScope.sendEvent(
            analytics = analytics,
            eventName = EventsDictionary.keychainPhraseScreenShow,
            props = Props(
                mapOf(EventsPropertiesKey.type to EventsDictionary.Type.beforeLogout)
            )
        )
    }

    class Factory @Inject constructor(
        private val logout: Logout,
        private val analytics: Analytics,
        private val appActionManager: AppActionManager,
        private val globalSubscriptionManager: GlobalSubscriptionManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LogoutWarningViewModel(
                logout = logout,
                analytics = analytics,
                appActionManager = appActionManager,
                globalSubscriptionManager = globalSubscriptionManager
            ) as T
        }
    }

    sealed class Command {
        data object Logout : Command()
        data class ShowError(val msg: String) : Command()
    }
}