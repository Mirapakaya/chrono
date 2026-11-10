package com.chronoio.chrono.di.main

import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.data.auth.repo.config.GatewayProvider
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.config.CacheStorage
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.Gateway
import com.chronoio.chrono.domain.config.TechSpaceProvider
import com.chronoio.chrono.domain.auth.interactor.PreloadRemainingSpaces
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.launch.RemainingSpacesPreloader
import com.chronoio.chrono.domain.misc.UrlBuilderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
object ConfigModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideGateway(
        configStorage: ConfigStorage,
        logger: Logger
    ): Gateway = GatewayProvider(
        configStorage = configStorage,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideUrlBuilder(
        getGateway: Gateway
    ): UrlBuilder = UrlBuilderImpl(
        gateway = getGateway
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideConfigProvider(): ConfigStorage = CacheStorage()

    @JvmStatic
    @Provides
    @Singleton
    fun provideTechSpaceProvider(
        configStorage: ConfigStorage
    ): TechSpaceProvider = configStorage

    @JvmStatic
    @Provides
    @Singleton
    fun dispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @JvmStatic
    @Provides
    @Singleton
    @Named(DEFAULT_APP_COROUTINE_SCOPE)
    fun applicationCoroutineScope() : CoroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )

    @JvmStatic
    @Provides
    @Singleton
    fun awaitAccountStartedManager(): AwaitAccountStartManager = AwaitAccountStartManager.Default

    @JvmStatic
    @Provides
    @Singleton
    fun preferredSpaceIdHolder(): PreferredSpaceIdHolder = PreferredSpaceIdHolder.Default

    @JvmStatic
    @Provides
    @Singleton
    fun remainingSpacesPreloader(
        preloadRemainingSpaces: PreloadRemainingSpaces
    ): RemainingSpacesPreloader = RemainingSpacesPreloader(preloadRemainingSpaces)

    const val DEFAULT_APP_COROUTINE_SCOPE = "Default application coroutine scope"
}