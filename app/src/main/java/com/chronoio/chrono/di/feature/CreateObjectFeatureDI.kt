package com.chronoio.chrono.di.feature

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.device.FileSharer
import com.chronoio.chrono.domain.media.UploadFile
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.feature_create_object.presentation.CreateObjectViewModelFactory
import com.chronoio.chrono.feature_create_object.presentation.NewCreateObjectViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [CreateObjectFeatureDependencies::class],
    modules = [
        CreateObjectFeatureModule::class,
        CreateObjectFeatureModule.Declarations::class
    ]
)
@PerScreen
interface CreateObjectFeatureComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: NewCreateObjectViewModel.VmParams,
            dependencies: CreateObjectFeatureDependencies
        ): CreateObjectFeatureComponent
    }

    fun viewModelFactory(): CreateObjectViewModelFactory
}

@Module
object CreateObjectFeatureModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUploadFile(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): UploadFile = UploadFile(repo = repo, dispatchers = dispatchers)

    @Module
    interface Declarations {
        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: CreateObjectViewModelFactory
        ): ViewModelProvider.Factory
    }
}

interface CreateObjectFeatureDependencies : ComponentDependencies {
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun spaceViewSubscriptionContainer(): SpaceViewSubscriptionContainer
    fun blockRepo(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun fileSharer(): FileSharer
}
