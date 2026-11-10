package com.chronoio.chrono.di.main

import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.widgets.WidgetDispatchEvent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DispatcherModule {

    @JvmStatic
    @Provides
    @Singleton
    fun providePayloadDispatcher(): Dispatcher<Payload> = Dispatcher.Default()

    @JvmStatic
    @Provides
    @Singleton
    fun provideWidgetEventDispatcher(): Dispatcher<WidgetDispatchEvent> = Dispatcher.Default()
}