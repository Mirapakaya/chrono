package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.domain.auth.interactor.GetMnemonic
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.presentation.keychain.KeychainPhraseViewModelFactory
import com.chronoio.chrono.ui.dashboard.DashboardMnemonicReminderDialog
import com.chronoio.chrono.ui.profile.KeychainPhraseDialog
import dagger.Module
import dagger.Provides
import dagger.Subcomponent


@Subcomponent(
    modules = [KeychainPhraseModule::class]
)
@PerScreen
interface KeychainPhraseSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun keychainPhraseModule(module: KeychainPhraseModule): Builder
        fun build(): KeychainPhraseSubComponent
    }

    fun inject(fragment: KeychainPhraseDialog)
    fun inject(fragment: DashboardMnemonicReminderDialog)
}

@Module
object KeychainPhraseModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideKeychainPhraseViewModelFactory(
        getMnemonic: GetMnemonic,
        analytics: Analytics
    ) = KeychainPhraseViewModelFactory(
        getMnemonic = getMnemonic,
        analytics = analytics
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetMnemonicUseCase(
        repository: AuthRepository
    ): GetMnemonic = GetMnemonic(
        repository = repository
    )
}