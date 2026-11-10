package com.chronoio.chrono.di.feature.templates

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.presentation.editor.cover.CoverImageHashProvider
import com.chronoio.chrono.presentation.editor.render.DefaultBlockViewRenderer
import com.chronoio.chrono.presentation.editor.toggle.ToggleStateHolder
import com.chronoio.chrono.presentation.templates.TemplateBlankViewModelFactory
import com.chronoio.chrono.presentation.widgets.collection.ResourceProvider
import com.chronoio.chrono.providers.DefaultCoverImageHashProvider
import com.chronoio.chrono.ui.templates.TemplateBlankFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(
    modules = [TemplateBlankModule::class],
    dependencies = [TemplateBlankDependencies::class]
)
@TemplateBlankScope
interface TemplateBlankComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: TemplateBlankDependencies): TemplateBlankComponent
    }

    fun inject(fragment: TemplateBlankFragment)
}

@Module
object TemplateBlankModule {

    @JvmStatic
    @TemplateBlankScope
    @Provides
    fun provideToggleHolder(): ToggleStateHolder = ToggleStateHolder.Default()

    @JvmStatic
    @TemplateBlankScope
    @Provides
    fun provideCoverImageHashProvider(): CoverImageHashProvider = DefaultCoverImageHashProvider()

    @JvmStatic
    @TemplateBlankScope
    @Provides
    fun provideViewModelFactory(
        renderer: DefaultBlockViewRenderer,
        storeOfRelations: StoreOfRelations,
        storeOfObjectTypes: StoreOfObjectTypes,
        fieldParser: FieldParser
    ): ViewModelProvider.Factory = TemplateBlankViewModelFactory(
        renderer = renderer,
        storeOfRelations = storeOfRelations,
        storeOfObjectTypes = storeOfObjectTypes,
        fieldParser = fieldParser
    )

    @Module
    interface Declarations {

        @TemplateBlankScope
        @Binds
        fun bindRenderer(
            defaultRenderer: DefaultBlockViewRenderer
        ): DefaultBlockViewRenderer
    }
}

interface TemplateBlankDependencies : ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun storeOfRelations(): StoreOfRelations
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun dateProvider(): DateProvider
    fun fieldsProvider(): FieldParser
    fun resourceProvider(): ResourceProvider
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class TemplateBlankScope