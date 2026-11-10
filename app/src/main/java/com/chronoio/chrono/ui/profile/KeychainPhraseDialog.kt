package com.chronoio.chrono.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chronoio.chrono.core_utils.ext.arg
import com.chronoio.chrono.databinding.DialogKeychainPhraseBinding
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.ui.dashboard.BaseMnemonicFragment

class KeychainPhraseDialog : BaseMnemonicFragment<DialogKeychainPhraseBinding>() {

    private val screenType get() = arg<String>(ARG_SCREEN_TYPE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.sendShowEvent(screenType)
    }

    override fun injectDependencies() {
        componentManager().keychainPhraseComponent.get().inject(this)
    }

    override fun releaseDependencies() {
        componentManager().keychainPhraseComponent.release()
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogKeychainPhraseBinding = DialogKeychainPhraseBinding.inflate(
        inflater, container, false
    )

    override val keychain: TextView by lazy { binding.keychain }
    override val anchor: View by lazy { binding.anchor }
    override val btnCopy: TextView by lazy { binding.btnCopy }

    companion object {
        const val ARG_SCREEN_TYPE = "arg.keychain.screen.type"
    }
}