package com.chronoio.chrono.di.feature.onboarding

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.presentation.onboarding.OnboardingViewModel
import com.chronoio.chrono.presentation.util.downloader.UriFileProvider
import com.chronoio.chrono.ui.onboarding.OnboardingFragment
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [OnboardingDependencies::class],
    modules = [
        OnboardingModule::class,
        OnboardingModule.Declarations::class
    ]
)
@PerScreen
interface OnboardingComponent {
    @Component.Factory
    interface Builder {
        fun create(dependencies: OnboardingDependencies): OnboardingComponent
    }

    fun inject(fragment: OnboardingFragment)
}

@Module
object OnboardingModule {
    @Module
    interface Declarations {
        @Binds
        @AuthScreenScope
        fun bindViewModelFactory(factory: OnboardingViewModel.Factory): ViewModelProvider.Factory
    }
}

interface OnboardingDependencies : ComponentDependencies {
    fun analytics(): Analytics
    fun context(): Context
    fun uriFileProvider(): UriFileProvider
}