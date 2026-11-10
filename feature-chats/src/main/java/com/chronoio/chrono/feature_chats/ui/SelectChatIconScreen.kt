package com.chronoio.chrono.feature_chats.ui

import androidx.compose.runtime.Composable
import com.chronoio.chrono.core_ui.widgets.EmojiPickerScreen
import com.chronoio.chrono.presentation.picker.EmojiPickerView

@Composable
fun SelectChatIconScreen(
    views: List<EmojiPickerView> = emptyList(),
    onEmojiClicked: (String) -> Unit,
    onQueryChanged: (String) -> Unit
) {
    EmojiPickerScreen(
        views = views,
        onEmojiClicked = onEmojiClicked,
        onQueryChanged = onQueryChanged
    )
}
