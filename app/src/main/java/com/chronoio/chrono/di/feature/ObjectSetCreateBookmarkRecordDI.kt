package com.chronoio.chrono.di.feature;

import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.core_utils.tools.UrlValidator
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.collections.AddObjectToCollection
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.objects.CreateBookmarkObject
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.sets.ObjectSetCreateBookmarkRecordViewModel
import com.chronoio.chrono.presentation.sets.ObjectSetSession
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.sets.modals.SetObjectCreateBookmarkRecordFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [ObjectSetCreateBookmarkRecordModule::class])
@PerDialog
interface ObjectSetCreateBookmarkRecordSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun module(module: ObjectSetCreateBookmarkRecordModule): Builder
        fun build(): ObjectSetCreateBookmarkRecordSubComponent
    }

    fun inject(fragment: SetObjectCreateBookmarkRecordFragment)
}

@Module
object ObjectSetCreateBookmarkRecordModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideObjectSetRecordViewModelFactory(
        createBookmarkObject: CreateBookmarkObject,
        urlValidator: UrlValidator,
        spaceManager: SpaceManager,
        objectState: MutableStateFlow<ObjectState>,
        dispatcher: Dispatcher<Payload>,
        addObjectToCollection: AddObjectToCollection,
        session: ObjectSetSession,
        storeOfRelations: StoreOfRelations,
        dateProvider: DateProvider
    ) = ObjectSetCreateBookmarkRecordViewModel.Factory(
        createBookmarkObject = createBookmarkObject,
        urlValidator = urlValidator,
        spaceManager = spaceManager,
        objectState = objectState,
        dispatcher = dispatcher,
        addObjectToCollection = addObjectToCollection,
        session = session,
        storeOfRelations = storeOfRelations,
        dateProvider = dateProvider
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideCreateBookmarkObjectUseCase(
        repo: BlockRepository
    ): CreateBookmarkObject = CreateBookmarkObject(
        repo = repo
    )
}