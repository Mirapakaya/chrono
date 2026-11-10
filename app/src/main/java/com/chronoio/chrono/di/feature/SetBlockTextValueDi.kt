package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.domain.block.interactor.UpdateText
import com.chronoio.chrono.domain.clipboard.Copy
import com.chronoio.chrono.domain.clipboard.Paste
import com.chronoio.chrono.presentation.editor.Editor
import com.chronoio.chrono.presentation.objects.block.SetBlockTextValueViewModel
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.editor.modals.SetBlockTextValueFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [SetBlockTextValueModule::class])
@PerDialog
interface SetBlockTextValueSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(model: SetBlockTextValueModule): Builder
        fun build(): SetBlockTextValueSubComponent
    }

    fun inject(fragment: SetBlockTextValueFragment)
}

@Module
object SetBlockTextValueModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        storage: Editor.Storage,
        dispatcher: Dispatcher<Payload>,
        paste: Paste,
        copy: Copy,
        updateText: UpdateText,
        analytics: Analytics
    ): SetBlockTextValueViewModel.Factory =
        SetBlockTextValueViewModel.Factory(
            storage = storage,
            dispatcher = dispatcher,
            paste = paste,
            copy = copy,
            updateText = updateText,
            analytics = analytics
        )
}