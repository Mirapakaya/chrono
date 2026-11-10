package com.chronoio.chrono.di.feature.templates

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.di.feature.onboarding.AuthScreenScope
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.templates.ApplyTemplate
import com.chronoio.chrono.domain.templates.GetTemplates
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.editor.cover.CoverImageHashProvider
import com.chronoio.chrono.presentation.onboarding.OnboardingViewModel
import com.chronoio.chrono.providers.DefaultCoverImageHashProvider
import com.chronoio.chrono.ui.templates.TemplateSelectFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(
    modules = [TemplateSelectModule::class],
    dependencies = [TemplateSelectDependencies::class]
)
@TemplateSelectScope
interface TemplateSelectComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: TemplateSelectDependencies): TemplateSelectComponent
    }

    fun inject(fragment: TemplateSelectFragment)
}

@Module
object TemplateSelectModule {
    @JvmStatic
    @Provides
    @TemplateSelectScope
    fun applyTemplate(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): ApplyTemplate =
        ApplyTemplate(
            repo = repo,
            dispatchers = dispatchers
        )

    @JvmStatic
    @Provides
    @TemplateSelectScope
    fun getTemplates(
        repo: BlockRepository,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers
    ): GetTemplates =
        GetTemplates(
            repo = repo,
            spaceManager = spaceManager,
            dispatchers = dispatchers
        )

    @JvmStatic
    @TemplateSelectScope
    @Provides
    fun provideCoverImageHashProvider(): CoverImageHashProvider = DefaultCoverImageHashProvider()

    @Module
    interface Declarations {
        @Binds
        @AuthScreenScope
        fun bindViewModelFactory(factory: OnboardingViewModel.Factory): ViewModelProvider.Factory
    }
}

interface TemplateSelectDependencies : ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun storeOfRelations(): StoreOfRelations
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun analytics() : Analytics
    fun blockRepository(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun spaceManager(): SpaceManager
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class TemplateSelectScope