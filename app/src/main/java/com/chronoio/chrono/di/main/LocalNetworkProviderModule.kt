package com.chronoio.chrono.di.main

import android.content.Context
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.middleware.discovery.MDNSDelegate
import com.chronoio.chrono.middleware.discovery.MDNSResolver
import com.chronoio.chrono.middleware.discovery.NsdDiscoveryListener
import com.chronoio.chrono.middleware.discovery.NsdRegistrationListener
import com.chronoio.chrono.middleware.discovery.adresshandler.DefaultInterfaceProvider
import com.chronoio.chrono.middleware.discovery.adresshandler.LocalNetworkAddressProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kotlinx.coroutines.GlobalScope
import service.InterfaceGetter

@Module(includes = [LocalNetworkProviderModule.Bindings::class])
class LocalNetworkProviderModule {

    @Singleton
    @Provides
    fun provideHandler(
        dispatchers: AppCoroutineDispatchers,
        interfaceGetter: InterfaceGetter,
    ): LocalNetworkAddressProvider =
        LocalNetworkAddressProvider(GlobalScope, dispatchers.io, interfaceGetter)

    @Singleton
    @Provides
    fun provideMDNSDelegate(
        dispatchers: AppCoroutineDispatchers,
        resolver: MDNSResolver
    ): MDNSDelegate = MDNSDelegate(GlobalScope, dispatchers.io, resolver)

    @Singleton
    @Provides
    fun provideNsdDiscoveryListener(
        nsdManager: NsdManager,
        dispatchers: AppCoroutineDispatchers,
    ): NsdDiscoveryListener = NsdDiscoveryListener(GlobalScope, dispatchers.io, nsdManager)

    @Singleton
    @Provides
    fun provideNsdManager(
        context: Context
    ): NsdManager = (context.getSystemService(Context.NSD_SERVICE) as NsdManager)

    @Singleton
    @Provides
    fun provideWifiManager(
        context: Context
    ): WifiManager = (context.getSystemService(Context.WIFI_SERVICE) as WifiManager)

    @Singleton
    @Provides
    fun provideMDNSResolver(
        nsdManager: NsdManager,
        wifiManager: WifiManager,
        discoveryListener: NsdDiscoveryListener,
        registrationListener: NsdRegistrationListener
    ): MDNSResolver = MDNSResolver(
        nsdManager,
        wifiManager,
        discoveryListener,
        registrationListener
    )

    @Module
    interface Bindings {

        @Singleton
        @Binds
        fun bindInterfaceGetter(
            provider: DefaultInterfaceProvider
        ): InterfaceGetter
    }
}