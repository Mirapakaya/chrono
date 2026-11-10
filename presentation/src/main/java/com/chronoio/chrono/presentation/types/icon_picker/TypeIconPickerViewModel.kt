package com.chronoio.chrono.presentation.types.icon_picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.emojifier.data.EmojiProvider
import com.chronoio.chrono.emojifier.suggest.EmojiSuggester
import com.chronoio.chrono.presentation.picker.IconPickerViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@Deprecated("Before Primitives, to delete")
class TypeIconPickerViewModel(
    provider: EmojiProvider,
    suggester: EmojiSuggester,
) : IconPickerViewModel<Unit>(provider, suggester) {

    val actions = MutableStateFlow<EmojiPickerAction>(EmojiPickerAction.Idle)

    override fun setEmoji(iconable: Unit, emojiUnicode: String) {
        actions.value = EmojiPickerAction.SetEmoji(emojiUnicode)
    }

    override fun onRemoveClicked(iconable: Unit) {
        actions.value = EmojiPickerAction.RemoveEmoji
    }

    override fun onPickedFromDevice(iconable: Unit, path: String, space: Id) {
        // Do nothing
    }

    sealed class EmojiPickerAction {
        object Idle : EmojiPickerAction()
        class SetEmoji(val emojiUnicode: String) : EmojiPickerAction()
        object RemoveEmoji : EmojiPickerAction()
    }

    class Factory @Inject constructor(
        private val emojiSuggester: EmojiSuggester,
        private val emojiProvider: EmojiProvider
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TypeIconPickerViewModel(
                suggester = emojiSuggester,
                provider = emojiProvider
            ) as T
        }
    }

}
