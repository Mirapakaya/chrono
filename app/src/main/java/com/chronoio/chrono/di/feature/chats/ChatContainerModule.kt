package com.chronoio.chrono.di.feature.chats

import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.chats.ChatContainer
import com.chronoio.chrono.domain.chats.ChatEventChannel
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

/** Shared by chats and discussions, which both collect a ChatContainer. */
@Module
object ChatContainerModule {
    @JvmStatic
    @Provides
    @PerScreen
    fun provideChatContainer(
        repo: BlockRepository,
        channel: ChatEventChannel,
        logger: Logger,
        subscription: StorelessSubscriptionContainer,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        dispatchers: AppCoroutineDispatchers
    ): ChatContainer = ChatContainer(
        repo, channel, logger, subscription,
        readScope = CoroutineScope(scope.coroutineContext + dispatchers.io)
    )
}
