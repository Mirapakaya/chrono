package com.chronoio.chrono.di.main


import android.content.Context
import com.chronoio.chrono.domain.icon.DocumentEmojiIconProvider
import com.chronoio.chrono.emojifier.data.DefaultDocumentEmojiIconProvider
import com.chronoio.chrono.emojifier.suggest.EmojiSuggester
import com.chronoio.chrono.emojifier.suggest.data.DefaultEmojiSuggestStorage
import com.chronoio.chrono.emojifier.suggest.data.DefaultEmojiSuggester
import com.chronoio.chrono.emojifier.suggest.data.EmojiSuggestStorage
import com.chronoio.chrono.emojifier.suggest.data.EmojiSuggesterCache
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object EmojiModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideEmojiSuggester(
        cache: EmojiSuggesterCache,
        storage: EmojiSuggestStorage
    ): EmojiSuggester {
        return DefaultEmojiSuggester(
            cache = cache,
            storage = storage
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideEmojiSuggesterCache(): EmojiSuggesterCache {
        return EmojiSuggesterCache.DefaultCache()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideEmojiSuggestStorage(context: Context, gson: Gson): EmojiSuggestStorage {
        return DefaultEmojiSuggestStorage(context, gson)
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideDocumentEmojiIconProvider(): DocumentEmojiIconProvider {
        return DefaultDocumentEmojiIconProvider()
    }
}