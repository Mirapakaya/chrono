package com.chronoio.chrono.di.main

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import com.chronoio.chrono.clipboard.AnytypeClipboard
import com.chronoio.chrono.clipboard.AnytypeClipboardStorage
import com.chronoio.chrono.clipboard.AnytypeUriMatcher
import com.chronoio.chrono.data.auth.mapper.Serializer
import com.chronoio.chrono.data.auth.other.ClipboardDataUriMatcher
import com.chronoio.chrono.data.auth.repo.clipboard.ClipboardDataRepository
import com.chronoio.chrono.data.auth.repo.clipboard.ClipboardDataStore
import com.chronoio.chrono.domain.clipboard.Clipboard
import com.chronoio.chrono.middleware.converters.ClipboardSerializer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ClipboardModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideClipboardRepository(
        factory: ClipboardDataStore.Factory
    ) : Clipboard = ClipboardDataRepository(factory)

    @JvmStatic
    @Provides
    @Singleton
    fun provideClipboardDataStoreFactory(
        storage: ClipboardDataStore.Storage,
        system: ClipboardDataStore.System
    ) : ClipboardDataStore.Factory = ClipboardDataStore.Factory(storage, system)

    @JvmStatic
    @Provides
    @Singleton
    fun provideClipboardStorage(
        context: Context,
        serializer: Serializer
    ) : ClipboardDataStore.Storage = AnytypeClipboardStorage(context, serializer)

    @JvmStatic
    @Provides
    @Singleton
    fun provideClipboardSystem(
        cm: ClipboardManager
    ) : ClipboardDataStore.System = AnytypeClipboard(cm)

    @JvmStatic
    @Provides
    @Singleton
    fun provideClipboardManager(
        context: Context
    ) : ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

    @JvmStatic
    @Provides
    @Singleton
    fun provideUriMatcher() : Clipboard.UriMatcher = ClipboardDataUriMatcher(
        matcher = AnytypeUriMatcher()
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideSerializer() : Serializer = ClipboardSerializer()
}