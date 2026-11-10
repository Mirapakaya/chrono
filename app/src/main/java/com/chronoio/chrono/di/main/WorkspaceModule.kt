package com.chronoio.chrono.di.main

import com.chronoio.chrono.data.auth.status.SyncAndP2PStatusDataChannel
import com.chronoio.chrono.data.auth.status.SyncAndP2PStatusEventsStore
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.domain.workspace.SyncAndP2PStatusChannel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object WorkspaceModule {

    @JvmStatic
    @Provides
    @Singleton
    fun spaces(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): SpaceManager = SpaceManager.Impl(
        dispatchers = dispatchers,
        repo = repo,
        logger = logger
    )

    @JvmStatic
    @Provides
    @Singleton
    fun spaceSyncStatusChannel(
        store: SyncAndP2PStatusEventsStore
    ): SyncAndP2PStatusChannel = SyncAndP2PStatusDataChannel(store)
}