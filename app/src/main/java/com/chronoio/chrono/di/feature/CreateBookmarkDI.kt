package com.chronoio.chrono.di.feature

import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.presentation.editor.bookmark.CreateBookmarkViewModel
import com.chronoio.chrono.ui.editor.modals.CreateBookmarkFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [CreateBookmarkModule::class])
@PerScreen
interface CreateBookmarkSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun createBookmarkModule(module: CreateBookmarkModule): Builder
        fun build(): CreateBookmarkSubComponent
    }

    fun inject(fragment: CreateBookmarkFragment)
}

@Module
class CreateBookmarkModule {

    @Provides
    @PerScreen
    fun provideCreateBookmarkViewModelFactory(): CreateBookmarkViewModel.Factory {
        return CreateBookmarkViewModel.Factory()
    }
}