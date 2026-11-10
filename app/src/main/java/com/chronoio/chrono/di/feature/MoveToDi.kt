package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.moving.MoveToViewModel
import com.chronoio.chrono.presentation.moving.MoveToViewModelFactory
import com.chronoio.chrono.ui.moving.MoveToFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [MoveToModule::class],
    dependencies = [MoveToDependencies::class]
)
@PerScreen
interface MoveToComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            vmParams: MoveToViewModel.VmParams,
            dependencies: MoveToDependencies
        ): MoveToComponent
    }

    fun inject(fragment: MoveToFragment)
}

interface MoveToDependencies : ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun searchObjects(): SearchObjects
    fun analytics(): Analytics
    fun analyticSpaceHelperDelegate(): AnalyticSpaceHelperDelegate
    fun  fieldParser(): FieldParser
    fun storeOfObjectTypes(): StoreOfObjectTypes
}

@Module
object MoveToModule {

    @JvmStatic
    @PerScreen
    @Provides
    fun provideMoveToViewModelFactory(
        vmParams: MoveToViewModel.VmParams,
        urlBuilder: UrlBuilder,
        searchObjects: SearchObjects,
        analytics: Analytics,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        fieldParser: FieldParser,
        storeOfObjectTypes: StoreOfObjectTypes
    ): MoveToViewModelFactory = MoveToViewModelFactory(
        vmParams = vmParams,
        urlBuilder = urlBuilder,
        searchObjects = searchObjects,
        analytics = analytics,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        fieldParser = fieldParser,
        storeOfObjectTypes = storeOfObjectTypes
    )
}