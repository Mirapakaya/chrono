package com.chronoio.chrono.di.feature.relations

import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.relations.LimitObjectTypeViewModel
import com.chronoio.chrono.presentation.relations.model.CreateFromScratchState
import com.chronoio.chrono.presentation.relations.model.StateHolder
import com.chronoio.chrono.ui.relations.LimitObjectTypeFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [LimitObjectTypeModule::class])
@PerDialog
interface LimitObjectTypeSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: LimitObjectTypeModule): Builder
        fun build(): LimitObjectTypeSubComponent
    }

    fun inject(fragment: LimitObjectTypeFragment)
}

@Module
object LimitObjectTypeModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        searchObjects: SearchObjects,
        urlBuilder: UrlBuilder,
        state: StateHolder<CreateFromScratchState>,
        spaceManager: SpaceManager
    ): LimitObjectTypeViewModel.Factory = LimitObjectTypeViewModel.Factory(
        searchObjects = searchObjects,
        urlBuilder = urlBuilder,
        state = state,
        spaceManager = spaceManager
    )
}