package com.chronoio.chrono.di.feature;

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.device.DefaultGradientCollectionProvider
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.cover.GetCoverGradientCollection
import com.chronoio.chrono.domain.cover.RemoveDocCover
import com.chronoio.chrono.domain.cover.SetDocCoverColor
import com.chronoio.chrono.domain.cover.SetDocCoverGradient
import com.chronoio.chrono.domain.cover.SetDocCoverImage
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.editor.cover.SelectCoverObjectSetViewModel
import com.chronoio.chrono.presentation.editor.cover.SelectCoverObjectViewModel
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.editor.cover.SelectCoverObjectFragment
import com.chronoio.chrono.ui.editor.cover.SelectCoverObjectSetFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [SelectCoverObjectModule::class])
@PerModal
interface SelectCoverObjectSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: SelectCoverObjectModule): Builder
        fun build(): SelectCoverObjectSubComponent
    }

    fun inject(fragment: SelectCoverObjectFragment)
}

@Module
object SelectCoverObjectModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetDocCoverColorUseCase(
        repo: BlockRepository
    ): SetDocCoverColor = SetDocCoverColor(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetDocCoverGradientUseCase(
        repo: BlockRepository
    ): SetDocCoverGradient = SetDocCoverGradient(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideRemoveDocCoverUseCase(
        repo: BlockRepository
    ): RemoveDocCover = RemoveDocCover(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSelectDocCoverViewModelFactory(
        setCoverImage: SetDocCoverImage,
        setCoverColor: SetDocCoverColor,
        setCoverGradient: SetDocCoverGradient,
        removeCover: RemoveDocCover,
        dispatcher: Dispatcher<Payload>,
        getCoverGradientCollection: GetCoverGradientCollection,
        analytics: Analytics,
        spaceManager: SpaceManager
    ): SelectCoverObjectViewModel.Factory = SelectCoverObjectViewModel.Factory(
        setCoverImage = setCoverImage,
        setCoverColor = setCoverColor,
        setCoverGradient = setCoverGradient,
        removeCover = removeCover,
        dispatcher = dispatcher,
        getCoverGradientCollection = getCoverGradientCollection,
        analytics = analytics,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @PerModal
    fun provideGetCoverGradientCollectionUseCase(
    ): GetCoverGradientCollection = GetCoverGradientCollection(DefaultGradientCollectionProvider)
}

@Subcomponent(modules = [SelectCoverObjectSetModule::class])
@PerModal
interface SelectCoverObjectSetSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: SelectCoverObjectSetModule): Builder
        fun build(): SelectCoverObjectSetSubComponent
    }

    fun inject(fragment: SelectCoverObjectSetFragment)
}

@Module
object SelectCoverObjectSetModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetDocCoverColorUseCase(
        repo: BlockRepository
    ): SetDocCoverColor = SetDocCoverColor(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetDocCoverGradientUseCase(
        repo: BlockRepository
    ): SetDocCoverGradient = SetDocCoverGradient(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideRemoveDocCoverUseCase(
        repo: BlockRepository
    ): RemoveDocCover = RemoveDocCover(repo)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSelectDocCoverViewModelFactory(
        setCoverImage: SetDocCoverImage,
        setCoverColor: SetDocCoverColor,
        setCoverGradient: SetDocCoverGradient,
        removeCover: RemoveDocCover,
        dispatcher: Dispatcher<Payload>,
        getCoverGradientCollection: GetCoverGradientCollection,
        analytics: Analytics,
        spaceManager: SpaceManager
    ): SelectCoverObjectSetViewModel.Factory = SelectCoverObjectSetViewModel.Factory(
        setCoverImage = setCoverImage,
        setCoverColor = setCoverColor,
        setCoverGradient = setCoverGradient,
        removeCover = removeCover,
        dispatcher = dispatcher,
        getCoverGradientCollection = getCoverGradientCollection,
        analytics = analytics,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @PerModal
    fun provideGetCoverGradientCollectionUseCase(
    ): GetCoverGradientCollection = GetCoverGradientCollection(DefaultGradientCollectionProvider)
}