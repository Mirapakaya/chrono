package com.chronoio.chrono.di.feature.discussions

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.di.feature.chats.ChatContainerModule
import com.chronoio.chrono.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.chats.ChatEventChannel
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.feature_discussions.presentation.DiscussionViewModel
import com.chronoio.chrono.feature_discussions.presentation.DiscussionViewModelFactory
import com.chronoio.chrono.presentation.util.CopyFileToCacheDirectory
import com.chronoio.chrono.presentation.util.defaultCopyFileToCacheDirectory
import com.chronoio.chrono.ui.discussions.DiscussionFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

@Component(
    dependencies = [DiscussionComponentDependencies::class],
    modules = [
        ChatContainerModule::class,
        DiscussionModule.Declarations::class,
        DiscussionModule.Providers::class
    ]
)
@PerScreen
interface DiscussionComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: DiscussionViewModel.Params): Builder
        fun withDependencies(dependencies: DiscussionComponentDependencies): Builder
        fun build(): DiscussionComponent
    }
    fun inject(fragment: DiscussionFragment)
}

@Module
object DiscussionModule {
    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: DiscussionViewModelFactory
        ): ViewModelProvider.Factory
    }

    @Module
    object Providers {
        @JvmStatic
        @Provides
        @PerScreen
        fun provideCopyFileToCache(
            context: Context
        ): CopyFileToCacheDirectory = defaultCopyFileToCacheDirectory(context)
    }
}

interface DiscussionComponentDependencies : ComponentDependencies {
    @Named(DEFAULT_APP_COROUTINE_SCOPE) fun scope(): CoroutineScope
    fun blockRepository(): BlockRepository
    fun authRepo(): AuthRepository
    fun appCoroutineDispatchers(): AppCoroutineDispatchers
    fun urlBuilder(): UrlBuilder
    fun chatEventChannel(): ChatEventChannel
    fun logger(): Logger
    fun members(): ActiveSpaceMemberSubscriptionContainer
    fun storelessSubscriptionContainer(): StorelessSubscriptionContainer
    fun dateProvider(): DateProvider
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun fieldParser(): FieldParser
    fun context(): Context
}
