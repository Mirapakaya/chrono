package com.chronoio.chrono.di.main

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.chronoio.chrono.device.network_type.NetworkConnectionStatusImpl
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.device.NetworkConnectionStatus
import com.chronoio.chrono.domain.network.NetworkModeProvider
import com.chronoio.chrono.persistence.networkmode.DefaultNetworkModeProvider
import com.chronoio.chrono.persistence.networkmode.DefaultNetworkModeProvider.NetworkModeConstants.NAMED_NETWORK_MODE_PREFS
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module
object NetworkModeModule {

    @JvmStatic
    @Provides
    @Singleton
    @Named(NAMED_NETWORK_MODE_PREFS)
    fun provideNetworkModeSharedPreferences(
        context: Context
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provider(
        @Named(NAMED_NETWORK_MODE_PREFS) sharedPreferences: SharedPreferences
    ): NetworkModeProvider = DefaultNetworkModeProvider(sharedPreferences)

    @JvmStatic
    @Provides
    @Singleton
    fun provideNetworkConnectionStatus(
        context: Context,
        dispatcher: AppCoroutineDispatchers,
        blockRepository: BlockRepository,
        @Named(ConfigModule.DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): NetworkConnectionStatus = NetworkConnectionStatusImpl(
        context = context,
        dispatchers = dispatcher,
        coroutineScope = scope,
        blockRepository = blockRepository
    )
}