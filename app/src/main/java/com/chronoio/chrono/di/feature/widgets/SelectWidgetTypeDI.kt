package com.chronoio.chrono.di.feature.widgets

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.widgets.UpdateWidget
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.widgets.SelectWidgetTypeViewModel
import com.chronoio.chrono.presentation.widgets.WidgetDispatchEvent
import com.chronoio.chrono.ui.widgets.SelectWidgetTypeFragment
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [SelectWidgetTypeModule::class],
    dependencies = [SelectWidgetTypeDependencies::class]
)
@PerModal
interface SelectWidgetTypeComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: SelectWidgetTypeDependencies): SelectWidgetTypeComponent
    }

    fun inject(fragment: SelectWidgetTypeFragment)
}

interface SelectWidgetTypeDependencies : ComponentDependencies {
    fun dispatcherWidgets(): Dispatcher<WidgetDispatchEvent>
    fun dispatcherPayload(): Dispatcher<Payload>
    fun blockRepository(): BlockRepository
    fun analytics(): Analytics
    fun urlBuilder(): UrlBuilder
    fun dispatchers(): AppCoroutineDispatchers
    fun storeOfObjectTypes(): StoreOfObjectTypes
}

@Module
object SelectWidgetTypeModule {

    @JvmStatic
    @PerModal
    @Provides
    fun factory(
        payloadDispatcher: Dispatcher<Payload>,
        widgetDispatcher: Dispatcher<WidgetDispatchEvent>,
        updateWidget: UpdateWidget,
        appCoroutineDispatchers: AppCoroutineDispatchers,
        analytics: Analytics
    ): SelectWidgetTypeViewModel.Factory = SelectWidgetTypeViewModel.Factory(
        payloadDispatcher = payloadDispatcher,
        widgetDispatcher = widgetDispatcher,
        updateWidget = updateWidget,
        appCoroutineDispatchers = appCoroutineDispatchers,
        analytics = analytics
    )
}