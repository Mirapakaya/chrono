package com.chronoio.chrono.features.emoji

import com.chronoio.chrono.presentation.editor.picker.ObjectIconPickerViewModelFactory
import com.chronoio.chrono.ui.editor.modals.ObjectIconPickerFragment

class TestDocumentEmojiPickerFragment : ObjectIconPickerFragment() {
    init {
        factory = testViewModelFactory
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    companion object {
        lateinit var testViewModelFactory: ObjectIconPickerViewModelFactory
    }
}