package com.chronoio.chrono.di.feature.chats

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.emojifier.data.Emoji
import com.chronoio.chrono.emojifier.data.EmojiProvider
import com.chronoio.chrono.emojifier.suggest.EmojiSuggester
import com.chronoio.chrono.feature_chats.presentation.SelectChatReactionViewModel
import com.chronoio.chrono.ui.chats.SelectChatReactionFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [SelectChatReactionDependencies::class],
    modules = [
        SelectChatReactionModule::class,
        SelectChatReactionModule.Declarations::class
    ]
)
@PerScreen
interface SelectChatReactionComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: SelectChatReactionViewModel.Params): Builder
        fun withDependencies(dependencies: SelectChatReactionDependencies): Builder
        fun build(): SelectChatReactionComponent
    }

    fun getViewModel(): SelectChatReactionViewModel
    fun inject(fragment: SelectChatReactionFragment)
}

@Module
object SelectChatReactionModule {
    @Provides
    @PerScreen
    @JvmStatic
    fun provideEmojiProvider(): EmojiProvider = Emoji

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: SelectChatReactionViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface SelectChatReactionDependencies : ComponentDependencies {
    fun dispatchers(): AppCoroutineDispatchers
    fun suggester(): EmojiSuggester
    fun repo(): BlockRepository
    fun auth(): AuthRepository
    fun prefs(): UserSettingsRepository
}