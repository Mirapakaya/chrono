package com.chronoio.chrono.di.main

import android.content.Context
import com.chronoio.chrono.data.auth.event.MembershipDateChannel
import com.chronoio.chrono.data.auth.event.MembershipRemoteChannel
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.domain.workspace.MembershipChannel
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.middleware.interactor.MembershipMiddlewareChannel
import com.chronoio.chrono.payments.playbilling.BillingClientLifecycle
import com.chronoio.chrono.presentation.membership.provider.MembershipProvider
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module
object MembershipModule {

    @Singleton
    @Provides
    fun provideBillingLifecycle(
        context: Context,
        dispatchers: AppCoroutineDispatchers,
        @Named(ConfigModule.DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope
    ): BillingClientLifecycle {
        return BillingClientLifecycle(
            dispatchers = dispatchers,
            applicationContext = context,
            scope = scope
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideMembershipRemoteChannel(
        proxy: EventProxy
    ): MembershipRemoteChannel = MembershipMiddlewareChannel(
        eventsProxy = proxy
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideMembershipChannel(
        channel: MembershipRemoteChannel
    ): MembershipChannel = MembershipDateChannel(
        channel = channel
    )

    @JvmStatic
    @Singleton
    @Provides
    fun provideMembershipProvider(
        dispatchers: AppCoroutineDispatchers,
        awaitAccountStartManager: AwaitAccountStartManager,
        membershipChannel: MembershipChannel,
        localeProvider: LocaleProvider,
        repo: BlockRepository,
        dateProvider: DateProvider
    ): MembershipProvider = MembershipProvider.Default(
        dispatchers = dispatchers,
        membershipChannel = membershipChannel,
        awaitAccountStartManager = awaitAccountStartManager,
        localeProvider = localeProvider,
        repo = repo,
        dateProvider = dateProvider
    )
}