package com.chronoio.chrono.di.feature.widgets

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.widgets.GetSuggestedWidgetTypes
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.search.ObjectSearchViewModel
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.widgets.SelectWidgetSourceViewModel
import com.chronoio.chrono.presentation.widgets.WidgetDispatchEvent
import com.chronoio.chrono.ui.widgets.SelectWidgetSourceFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [SelectWidgetSourceModule::class],
    dependencies = [SelectWidgetSourceDependencies::class]
)
@PerModal
interface SelectWidgetSourceComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            vmParams: ObjectSearchViewModel.VmParams,
            dependencies: SelectWidgetSourceDependencies
        ): SelectWidgetSourceComponent
    }

    fun inject(fragment: SelectWidgetSourceFragment)
}

interface SelectWidgetSourceDependencies : ComponentDependencies {
    fun dispatcherWidgets(): Dispatcher<WidgetDispatchEvent>
    fun blockRepository(): BlockRepository
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun analyticsHelper(): AnalyticSpaceHelperDelegate
    fun searchObjects(): SearchObjects
    fun fieldParser(): FieldParser
    fun logger(): Logger
    fun spaceViews(): SpaceViewSubscriptionContainer
}

@Module
object SelectWidgetSourceModule {

    @JvmStatic
    @PerModal
    @Provides
    fun factory(
        vmParams: ObjectSearchViewModel.VmParams,
        urlBuilder: UrlBuilder,
        analytics: Analytics,
        searchObjects: SearchObjects,
        dispatcher: Dispatcher<WidgetDispatchEvent>,
        analyticsHelper: AnalyticSpaceHelperDelegate,
        fieldParser: FieldParser,
        storeOfObjectTypes: StoreOfObjectTypes,
        getSuggestedWidgetTypes: GetSuggestedWidgetTypes,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer
    ): SelectWidgetSourceViewModel.Factory = SelectWidgetSourceViewModel.Factory(
        vmParams = vmParams,
        urlBuilder = urlBuilder,
        searchObjects = searchObjects,
        analytics = analytics,
        dispatcher = dispatcher,
        analyticSpaceHelperDelegate = analyticsHelper,
        fieldParser = fieldParser,
        storeOfObjectTypes = storeOfObjectTypes,
        getSuggestedWidgetTypes = getSuggestedWidgetTypes,
        spaceViews = spaceViewSubscriptionContainer
    )
}