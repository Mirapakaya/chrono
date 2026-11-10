package com.chronoio.chrono.presentation.editor.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.icon.RemoveDocumentIcon
import com.chronoio.chrono.domain.icon.SetDocumentEmojiIcon
import com.chronoio.chrono.domain.icon.SetDocumentImageIcon
import com.chronoio.chrono.emojifier.data.EmojiProvider
import com.chronoio.chrono.emojifier.suggest.EmojiSuggester
import com.chronoio.chrono.presentation.util.Dispatcher

class ObjectIconPickerViewModelFactory(
    private val setEmojiIcon: SetDocumentEmojiIcon,
    private val setImageIcon: SetDocumentImageIcon,
    private val removeDocumentIcon: RemoveDocumentIcon,
    private val emojiSuggester: EmojiSuggester,
    private val emojiProvider: EmojiProvider,
    private val dispatcher: Dispatcher<Payload>,
    private val analytics: Analytics
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ObjectIconPickerViewModel(
            setEmojiIcon = setEmojiIcon,
            setImageIcon = setImageIcon,
            removeDocumentIcon = removeDocumentIcon,
            suggester = emojiSuggester,
            provider = emojiProvider,
            dispatcher = dispatcher,
            analytics = analytics
        ) as T
    }
}

class ObjectSetIconPickerViewModelFactory(
    private val setEmojiIcon: SetDocumentEmojiIcon,
    private val setImageIcon: SetDocumentImageIcon,
    private val removeDocumentIcon: RemoveDocumentIcon,
    private val emojiSuggester: EmojiSuggester,
    private val emojiProvider: EmojiProvider,
    private val dispatcher: Dispatcher<Payload>,
    private val analytics: Analytics
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ObjectIconPickerViewModel(
            setEmojiIcon = setEmojiIcon,
            setImageIcon = setImageIcon,
            removeDocumentIcon = removeDocumentIcon,
            suggester = emojiSuggester,
            provider = emojiProvider,
            dispatcher = dispatcher,
            analytics = analytics
        ) as T
    }
}