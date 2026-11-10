package com.chronoio.chrono.di.main

import android.content.Context
import com.chronoio.chrono.CrashReporter
import com.chronoio.chrono.SentryCrashReporter
import com.chronoio.chrono.core_utils.tools.AppInfo
import com.chronoio.chrono.presentation.device.BuildProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object CrashReportingModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideCrashReporter(
        context: Context,
        appInfo: AppInfo,
        buildProvider: BuildProvider
    ): CrashReporter = SentryCrashReporter(
        context = context,
        appInfo = appInfo,
        withTimber = !buildProvider.isDebug()
    )
}