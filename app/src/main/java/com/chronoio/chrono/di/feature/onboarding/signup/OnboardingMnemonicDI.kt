package com.chronoio.chrono.di.feature.onboarding.signup

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.interactor.GetMnemonic
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.device.NetworkConnectionStatus
import com.chronoio.chrono.domain.network.NetworkModeProvider
import com.chronoio.chrono.presentation.onboarding.signup.OnboardingMnemonicViewModel
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(
    dependencies = [OnboardingMnemonicDependencies::class],
    modules = [
        OnboardingMnemonicModule::class,
        OnboardingMnemonicModule.Declarations::class
    ]
)
@MnemonicScreenScope
interface OnboardingMnemonicComponent {

    @Component.Factory
    interface Builder {
        fun create(dependencies: OnboardingMnemonicDependencies): OnboardingMnemonicComponent
    }

    fun getViewModel(): OnboardingMnemonicViewModel
}

@Module
object OnboardingMnemonicModule {

    @JvmStatic
    @Provides
    @MnemonicScreenScope
    fun provideGetMnemonicUseCase(
        authRepository: AuthRepository,
    ): GetMnemonic = GetMnemonic(
        repository = authRepository
    )
    
    @Module
    interface Declarations {

        @Binds
        @MnemonicScreenScope
        fun bindViewModelFactory(factory: OnboardingMnemonicViewModel.Factory): ViewModelProvider.Factory

    }
}

interface OnboardingMnemonicDependencies : ComponentDependencies {
    fun authRepository(): AuthRepository
    fun analytics(): Analytics
    fun networkModeProvider(): NetworkModeProvider
    fun networkConnectionStatus(): NetworkConnectionStatus
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MnemonicScreenScope