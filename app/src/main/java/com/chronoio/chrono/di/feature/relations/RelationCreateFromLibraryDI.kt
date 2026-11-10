package com.chronoio.chrono.di.feature.relations

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_models.RelationFormat
import com.chronoio.chrono.core_utils.di.scope.CreateFromScratch
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.objects.DefaultStoreOfRelations
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.relations.CreateRelation
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.relations.RelationCreateFromLibraryViewModel
import com.chronoio.chrono.presentation.relations.model.CreateFromScratchState
import com.chronoio.chrono.presentation.relations.model.StateHolder
import com.chronoio.chrono.ui.relations.RelationCreateFromLibraryFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [RelationCreateFromLibraryDependencies::class],
    modules = [
        RelationCreateFromLibraryModule::class,
        RelationCreateFromLibraryModule.Declarations::class
    ]
)
@PerScreen
@CreateFromScratch
interface RelationCreateFromLibraryComponent {

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: RelationCreateFromLibraryDependencies
        ): RelationCreateFromLibraryComponent
    }

    fun relationFormatPickerComponent(): RelationFormatPickerSubcomponent.Builder
    fun limitObjectTypeComponent(): LimitObjectTypeSubComponent.Builder

    fun inject(fragment: RelationCreateFromLibraryFragment)
}

@Module
object RelationCreateFromLibraryModule {

    @JvmStatic
    @Provides
    @CreateFromScratch
    fun provideState(): StateHolder<CreateFromScratchState> = StateHolder(
        initial = CreateFromScratchState(
            format = RelationFormat.OBJECT,
            limitObjectTypes = emptyList()
        )
    )

    @JvmStatic
    @Provides
    fun createRelation(
        repo: BlockRepository,
        storeOfRelations: StoreOfRelations
    ) = CreateRelation(
        repo = repo,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    fun provideSearchObjects(
        repo: BlockRepository
    ) = SearchObjects(
        repo = repo
    )

    @JvmStatic
    @Provides
    fun provideStoreOfRelations(): StoreOfRelations = DefaultStoreOfRelations()

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(factory: RelationCreateFromLibraryViewModel.Factory): ViewModelProvider.Factory
    }

}

interface RelationCreateFromLibraryDependencies : ComponentDependencies {
    fun blockRepository(): BlockRepository
    fun urlBuilder(): UrlBuilder
    fun spaceManager(): SpaceManager
}