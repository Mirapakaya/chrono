package com.chronoio.chrono.presentation.editor.editor.mention

sealed class MentionEvent {
    data class MentionSuggestText(val text: CharSequence) : MentionEvent()
    data class MentionSuggestStart(val cursorCoordinate : Int, val mentionStart: Int) : MentionEvent()
    object MentionSuggestStop : MentionEvent()
    object MentionSuggestStopCell : MentionEvent()
}