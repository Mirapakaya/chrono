package com.chronoio.chrono.di.main

import android.content.Context
import com.chronoio.chrono.data.auth.other.DataDownloader
import com.chronoio.chrono.data.auth.other.Device
import com.chronoio.chrono.device.providers.DefaultLocalProvider
import com.chronoio.chrono.device.SharedFileUploader
import com.chronoio.chrono.device.base.AndroidDevice
import com.chronoio.chrono.device.download.AndroidDeviceDownloader
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.device.FileSharer
import com.chronoio.chrono.domain.download.Downloader
import com.chronoio.chrono.domain.misc.LocaleProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DeviceModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideDownloader(
        device: Device
    ): Downloader = DataDownloader(device = device)

    @JvmStatic
    @Provides
    @Singleton
    fun provideDevice(
        downloader: AndroidDeviceDownloader
    ): Device = AndroidDevice(downloader = downloader)

    @JvmStatic
    @Provides
    @Singleton
    fun provideDeviceDownloader(
        context: Context
    ): AndroidDeviceDownloader = AndroidDeviceDownloader(context = context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideLocaleProvider(context: Context): LocaleProvider = DefaultLocalProvider(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideFileSharer(
        context: Context,
        dispatchers: AppCoroutineDispatchers
    ): FileSharer = SharedFileUploader(
        context = context,
        dispatchers = dispatchers
    )
}