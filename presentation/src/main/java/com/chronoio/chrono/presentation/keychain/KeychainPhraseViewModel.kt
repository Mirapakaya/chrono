package com.chronoio.chrono.presentation.keychain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.analytics.base.EventsDictionary
import com.chronoio.chrono.analytics.base.EventsDictionary.keychainCopy
import com.chronoio.chrono.analytics.base.EventsDictionary.keychainPhraseScreenShow
import com.chronoio.chrono.analytics.base.EventsPropertiesKey
import com.chronoio.chrono.analytics.base.sendEvent
import com.chronoio.chrono.analytics.props.Props
import com.chronoio.chrono.core_utils.ui.ViewStateViewModel
import com.chronoio.chrono.domain.auth.interactor.GetMnemonic
import timber.log.Timber

class KeychainPhraseViewModel(
    private val getMnemonic: GetMnemonic,
    private val analytics: Analytics
) : ViewStateViewModel<KeychainViewState>() {

    private var mnemonic: String = ""

    init {
        proceedWithGettingMnemonic()
        stateData.postValue(KeychainViewState.Blurred)
    }

    fun sendShowEvent(type: String) {
        viewModelScope.sendEvent(
            analytics = analytics,
            eventName = keychainPhraseScreenShow,
            props = Props(
                mapOf(EventsPropertiesKey.type to type)
            )
        )
    }

    private fun copyClickedFromScreenSettings() {
        viewModelScope.sendEvent(
            analytics = analytics,
            eventName = keychainCopy,
            props = Props(
                mapOf(EventsPropertiesKey.type to EventsDictionary.Type.screenSettings)
            )
        )
    }

    fun onKeychainClicked() {
        stateData.postValue(KeychainViewState.Displayed(mnemonic))
        copyClickedFromScreenSettings()
    }

    fun onRootClicked() {
        stateData.postValue(KeychainViewState.Blurred)
    }

    private fun proceedWithGettingMnemonic() {
        getMnemonic.invoke(viewModelScope, Unit) { result ->
            result.either(
                fnL = { e -> Timber.e(e, "Error while getting mnemonic") },
                fnR = { mnemonic ->
                    this.mnemonic = mnemonic
                    Any()
                }
            )
        }
    }
}

class KeychainPhraseViewModelFactory(
    private val getMnemonic: GetMnemonic,
    private val analytics: Analytics
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return KeychainPhraseViewModel(
            getMnemonic = getMnemonic,
            analytics = analytics
        ) as T
    }
}