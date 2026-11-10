package com.chronoio.chrono.di.feature.chats

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.emojifier.data.Emoji
import com.chronoio.chrono.emojifier.data.EmojiProvider
import com.chronoio.chrono.emojifier.suggest.EmojiSuggester
import com.chronoio.chrono.feature_chats.presentation.SelectChatIconViewModel
import com.chronoio.chrono.ui.chats.SelectChatIconFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [SelectChatIconDependencies::class],
    modules = [
        SelectChatIconModule::class,
        SelectChatIconModule.Declarations::class
    ]
)
@PerScreen
interface SelectChatIconComponent {
    @Component.Builder
    interface Builder {
        fun withDependencies(dependencies: SelectChatIconDependencies): Builder
        fun build(): SelectChatIconComponent
    }

    fun getViewModel(): SelectChatIconViewModel
    fun inject(fragment: SelectChatIconFragment)
}

@Module
object SelectChatIconModule {
    @Provides
    @PerScreen
    @JvmStatic
    fun provideEmojiProvider(): EmojiProvider = Emoji

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: SelectChatIconViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface SelectChatIconDependencies : ComponentDependencies {
    fun dispatchers(): AppCoroutineDispatchers
    fun suggester(): EmojiSuggester
}
