package com.chronoio.chrono.di.feature.chats

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import com.chronoio.chrono.feature_chats.presentation.ChatReactionViewModel
import com.chronoio.chrono.ui.chats.ChatReactionFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(
    dependencies = [ChatReactionDependencies::class],
    modules = [
        ChatReactionModule::class,
        ChatReactionModule.Declarations::class
    ]
)
@PerScreen
interface ChatReactionComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: ChatReactionViewModel.Params): Builder
        fun withDependencies(dependencies: ChatReactionDependencies): Builder
        fun build(): ChatReactionComponent
    }

    fun inject(fragment: ChatReactionFragment)
}

@Module
object ChatReactionModule {

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: ChatReactionViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface ChatReactionDependencies : ComponentDependencies {
    fun dispatchers(): AppCoroutineDispatchers
    fun repo(): BlockRepository
    fun auth(): AuthRepository
    fun urlBuilder(): UrlBuilder
    fun members(): ActiveSpaceMemberSubscriptionContainer
}