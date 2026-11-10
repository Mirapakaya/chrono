package com.chronoio.chrono.di.feature

import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.GetDebugSettings
import com.chronoio.chrono.domain.config.InfrastructureRepository
import com.chronoio.chrono.domain.config.UseCustomContextMenu
import com.chronoio.chrono.domain.debugging.DebugLocalStore
import com.chronoio.chrono.domain.debugging.DebugSpace
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.ui.settings.DebugSettingsFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [DebugSettingsModule::class])
@PerScreen
interface DebugSettingsSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: DebugSettingsModule): Builder
        fun build(): DebugSettingsSubComponent
    }

    fun inject(fragment: DebugSettingsFragment)
}

@Module
class DebugSettingsModule {

    @Provides
    @PerScreen
    fun provideUseCustomContextMenu(repo: InfrastructureRepository): UseCustomContextMenu =
        UseCustomContextMenu(repo)

    @Provides
    @PerScreen
    fun provideGetDebugSettings(
        repo: InfrastructureRepository
    ): GetDebugSettings = GetDebugSettings(
        repo = repo
    )

    @Provides
    @PerScreen
    fun provideDebugSync(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        spaceManager: SpaceManager
    ): DebugSpace = DebugSpace(
        repo = repo,
        dispatchers = dispatchers,
        spaceManager = spaceManager
    )

    @Provides
    @PerScreen
    fun provideDebugLocalStore(repo: BlockRepository) : DebugLocalStore = DebugLocalStore(repo)
}