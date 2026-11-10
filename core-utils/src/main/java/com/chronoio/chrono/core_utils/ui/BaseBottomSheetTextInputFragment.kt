package com.chronoio.chrono.core_utils.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.viewbinding.ViewBinding
import com.chronoio.chrono.core_utils.BuildConfig
import com.chronoio.chrono.core_utils.ext.showKeyboard
import com.chronoio.chrono.core_utils.ext.syncFocusWithImeVisibility

abstract class BaseBottomSheetTextInputFragment<T : ViewBinding>(
    fragmentScope: Boolean = true
) : BaseBottomSheetFragment<T>(fragmentScope = fragmentScope) {

    abstract val textInput: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindowInsetAnimation()
        textInput.showKeyboard()
    }

    private fun setupWindowInsetAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            textInput.syncFocusWithImeVisibility()
        }
    }
}