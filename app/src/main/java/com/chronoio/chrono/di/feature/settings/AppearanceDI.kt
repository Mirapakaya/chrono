package com.chronoio.chrono.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.theme.GetTheme
import com.chronoio.chrono.domain.theme.SetTheme
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.ui.settings.AppearanceFragment
import com.chronoio.chrono.ui_settings.appearance.AppearanceViewModel
import dagger.Component
import com.chronoio.chrono.ui_settings.appearance.ThemeApplicator
import com.chronoio.chrono.ui_settings.appearance.ThemeApplicatorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [AppearanceDependencies::class],
    modules = [
        AppearanceModule::class,
        AppearanceModule.Declarations::class
    ]
)
@PerScreen
interface AppearanceComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: AppearanceDependencies): AppearanceComponent
    }

    fun inject(fragment: AppearanceFragment)
}

@Module
object AppearanceModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetThemeUseCase(repo: UserSettingsRepository): GetTheme = GetTheme(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetThemeUseCase(repo: UserSettingsRepository): SetTheme = SetTheme(repo)

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: AppearanceViewModel.Factory
        ): ViewModelProvider.Factory

        @PerScreen
        @Binds
        fun bindThemeApplicator(applicator: ThemeApplicatorImpl): ThemeApplicator
    }
}

interface AppearanceDependencies : ComponentDependencies {
    fun userUserSettingsRepository(): UserSettingsRepository
    fun analytics(): Analytics
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
}