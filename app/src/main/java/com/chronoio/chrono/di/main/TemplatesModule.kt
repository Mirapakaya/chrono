package com.chronoio.chrono.di.main

import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.search.SubscriptionEventChannel
import com.chronoio.chrono.presentation.templates.DefaultObjectTypeTemplatesContainer
import com.chronoio.chrono.presentation.templates.ObjectTypeTemplatesContainer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object TemplatesModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideStorelessContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): StorelessSubscriptionContainer =
        StorelessSubscriptionContainer.Impl(repo, channel, dispatchers, logger)

    @JvmStatic
    @Provides
    @Singleton
    fun provideTemplatesContainer(
        storage: StorelessSubscriptionContainer
    ): ObjectTypeTemplatesContainer =
        DefaultObjectTypeTemplatesContainer(
            storage = storage
        )
}