package com.chronoio.chrono.di.feature

import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.page.CreateObjectByTypeAndTemplate
import com.chronoio.chrono.domain.templates.GetTemplates
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.objects.CreateObjectViewModel
import com.chronoio.chrono.ui.editor.CreateObjectFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [CreateObjectModule::class])
@PerScreen
interface CreateObjectSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: CreateObjectModule): Builder
        fun build(): CreateObjectSubComponent
    }

    fun inject(fragment: CreateObjectFragment)
}

@Module
object CreateObjectModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun getCreateObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): CreateObjectByTypeAndTemplate = CreateObjectByTypeAndTemplate(
        repo = repo,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun provideGetDefaultPageType(
        blockRepository: BlockRepository,
        userSettingsRepository: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = userSettingsRepository,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetTemplates(
        repo: BlockRepository,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers
    ): GetTemplates = GetTemplates(
        repo = repo,
        spaceManager = spaceManager,
        dispatchers = dispatchers
    )


    @JvmStatic
    @Provides
    @PerScreen
    fun provideViewModelFactory(
        createObject: CreateObjectByTypeAndTemplate,
        spaceManager: SpaceManager,
        awaitAccountStartManager: AwaitAccountStartManager
    ): CreateObjectViewModel.Factory = CreateObjectViewModel.Factory(
        createObject = createObject,
        spaceManager = spaceManager,
        awaitAccountStart = awaitAccountStartManager
    )
}