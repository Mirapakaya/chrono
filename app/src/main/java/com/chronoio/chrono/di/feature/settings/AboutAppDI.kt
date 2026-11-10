package com.chronoio.chrono.di.feature.settings

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.interactor.GetAccount
import com.chronoio.chrono.domain.auth.interactor.GetLibraryVersion
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.ui.settings.AboutAppFragment
import com.chronoio.chrono.ui_settings.about.AboutAppViewModel
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(
    modules = [AboutAppModule::class],
    dependencies = [AboutAppDependencies::class]
)
@AboutAppScope
interface AboutAppComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: AboutAppDependencies): AboutAppComponent
    }

    fun inject(fragment: AboutAppFragment)
}

@Module
object AboutAppModule {

    @JvmStatic
    @Provides
    @AboutAppScope
    fun provideGetAccountUseCase(
        repo: AuthRepository,
        dispatcher: AppCoroutineDispatchers
    ): GetAccount = GetAccount(repo = repo, dispatcher = dispatcher)

    @JvmStatic
    @Provides
    @AboutAppScope
    fun provideGetVersion(
        repo: AuthRepository
    ): GetLibraryVersion = GetLibraryVersion(repo)

    @Module
    interface Declarations {

        @AboutAppScope
        @Binds
        fun bindViewModelFactory(
            factory: AboutAppViewModel.Factory
        ): ViewModelProvider.Factory
    }
}

interface AboutAppDependencies : ComponentDependencies {
    fun authRepo(): AuthRepository
    fun configStorage(): ConfigStorage
    fun analytics(): Analytics
    fun dispatchers(): AppCoroutineDispatchers
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AboutAppScope