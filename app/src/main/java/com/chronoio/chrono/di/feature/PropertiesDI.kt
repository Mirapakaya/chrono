package com.chronoio.chrono.di.feature

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.dataview.SetDataViewProperties
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.`object`.SetObjectDetails
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.primitives.SetObjectTypeRecommendedFields
import com.chronoio.chrono.domain.relations.CreateRelation
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.feature_properties.EditTypePropertiesViewModelFactory
import com.chronoio.chrono.feature_properties.add.EditTypePropertiesVmParams
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.primitives.EditTypePropertiesFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

//region EDIT OBJECT TYPE PROPERTIES SCREEN
@PerModal
@Component(
    modules = [
        EditTypePropertiesModule::class,
        EditTypePropertiesModule.Declarations::class
    ],
    dependencies = [EditTypePropertiesDependencies::class]
)
interface EditTypePropertiesComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: EditTypePropertiesVmParams,
            dependencies: EditTypePropertiesDependencies
        ): EditTypePropertiesComponent
    }

    fun inject(fragment: EditTypePropertiesFragment)
}

@Module
object EditTypePropertiesModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideTypeSetRecommendedFields(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectTypeRecommendedFields = SetObjectTypeRecommendedFields(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun createRelation(
        repo: BlockRepository,
        storeOfRelations: StoreOfRelations,
    ) = CreateRelation(repo, storeOfRelations)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetObjectDetails(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetDataViewProperties(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetDataViewProperties = SetDataViewProperties(repo, dispatchers)

    @Module
    interface Declarations {
        @PerModal
        @Binds
        fun bindViewModelFactory(
            factory: EditTypePropertiesViewModelFactory
        ): ViewModelProvider.Factory
    }
}

interface EditTypePropertiesDependencies : ComponentDependencies {
    fun provideStringResourceProvider(): StringResourceProvider
    fun provideStoreOfRelations(): StoreOfRelations
    fun provideStoreOfObjectTypes(): StoreOfObjectTypes
    fun provideBlockRepository(): BlockRepository
    fun provideAppCoroutineDispatchers(): AppCoroutineDispatchers
    fun urlBuilder(): UrlBuilder
    fun dispatcher(): Dispatcher<Payload>
}
//endregion