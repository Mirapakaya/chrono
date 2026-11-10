package com.chronoio.chrono.di.feature.cover

import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.cover.SetDocCoverImage
import com.chronoio.chrono.domain.unsplash.SearchUnsplashImage
import com.chronoio.chrono.domain.unsplash.UnsplashRepository
import com.chronoio.chrono.presentation.common.Action
import com.chronoio.chrono.presentation.common.Delegator
import com.chronoio.chrono.presentation.editor.cover.UnsplashViewModel
import com.chronoio.chrono.ui.editor.cover.UnsplashBaseFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [UnsplashModule::class])
@PerDialog
interface UnsplashSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: UnsplashModule): Builder
        fun build(): UnsplashSubComponent
    }

    fun inject(fragment: UnsplashBaseFragment)
}

@Module
object UnsplashModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        search: SearchUnsplashImage,
        delegator: Delegator<Action>
    ): UnsplashViewModel.Factory {
        return UnsplashViewModel.Factory(
            search = search,
            delegator = delegator
        )
    }

    @JvmStatic
    @Provides
    @PerDialog
    fun provideSearch(repo: UnsplashRepository): SearchUnsplashImage = SearchUnsplashImage(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideSetDocCoverImageUseCase(
        repo: BlockRepository
    ): SetDocCoverImage = SetDocCoverImage(repo)
}