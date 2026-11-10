package com.chronoio.chrono.emojifier.suggest.data

import com.chronoio.chrono.emojifier.suggest.model.EmojiSuggest

interface EmojiSuggestStorage {
    suspend fun fetch(): List<EmojiSuggest>
}