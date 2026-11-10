package com.chronoio.chrono.di.feature.quickcapture

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.ai.TypeSuggestionEngine
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.multiplayer.ParticipantSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.ui.quickcapture.QuickCaptureFragment
import dagger.Component
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope
import com.chronoio.chrono.di.main.ConfigModule

@Component(
    dependencies = [QuickCaptureDependencies::class]
)
@PerScreen
interface QuickCaptureComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: QuickCaptureDependencies): QuickCaptureComponent
    }

    fun inject(fragment: QuickCaptureFragment)
}

interface QuickCaptureDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun appCoroutineDispatchers(): AppCoroutineDispatchers
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun userSettingsRepository(): UserSettingsRepository
    fun spaceManager(): SpaceManager
    fun userPermissionProvider(): UserPermissionProvider
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun provideStorelessSubscriptionContainer(): StorelessSubscriptionContainer
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun spaceSyncAndP2PStatusProvider(): SpaceSyncAndP2PStatusProvider
    fun typeSuggestionEngine(): TypeSuggestionEngine
    fun participantSubscriptionContainer(): ParticipantSubscriptionContainer
    fun authRepository(): AuthRepository
    @Named(ConfigModule.DEFAULT_APP_COROUTINE_SCOPE)
    fun applicationCoroutineScope(): CoroutineScope
    fun logger(): Logger
}
