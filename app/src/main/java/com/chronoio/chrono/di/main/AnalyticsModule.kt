package com.chronoio.chrono.di.main

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.analytics.base.DefaultAnalytics
import com.chronoio.chrono.analytics.tracker.AmplitudeTracker
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.analytics.DefaultAnalyticsParamsProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.GlobalScope
import javax.inject.Singleton

@Module
object AnalyticsModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideAnalytics(): Analytics = DefaultAnalytics()

    @JvmStatic
    @Provides
    @Singleton
    fun provideAmplitudeTracker(
        analytics: Analytics
    ): AmplitudeTracker = AmplitudeTracker(
        scope = GlobalScope,
        analytics = analytics
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideAnalyticsParams(
        userPermissionProvider: UserPermissionProvider,
        spaceViewContainer: SpaceViewSubscriptionContainer
    ): AnalyticSpaceHelperDelegate = DefaultAnalyticsParamsProvider(
        userPermissionProvider = userPermissionProvider,
        spaceViewContainer = spaceViewContainer
    )
}