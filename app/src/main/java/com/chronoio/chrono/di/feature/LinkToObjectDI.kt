package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.block.interactor.sets.GetObjectTypes
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.linking.LinkToObjectViewModelFactory
import com.chronoio.chrono.presentation.search.ObjectSearchViewModel
import com.chronoio.chrono.ui.linking.LinkToObjectFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [LinkToObjectModule::class],
    dependencies = [LinkToObjectDependencies::class]
)
@PerScreen
interface LinkToObjectComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            params: ObjectSearchViewModel.VmParams,
            dependencies: LinkToObjectDependencies
        ) : LinkToObjectComponent
    }

    fun inject(fragment: LinkToObjectFragment)
}

interface LinkToObjectDependencies: ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun getObjectTypes(): GetObjectTypes
    fun searchObjects(): SearchObjects
    fun analytics(): Analytics
    fun analyticSpaceHelperDelegate(): AnalyticSpaceHelperDelegate
    fun dateProvider(): DateProvider
    fun fieldParser(): FieldParser
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun spaceViews(): SpaceViewSubscriptionContainer
}

@Module
object LinkToObjectModule {

    @JvmStatic
    @PerScreen
    @Provides
    fun provideLinkToObjectViewModelFactory(
        vmParams: ObjectSearchViewModel.VmParams,
        urlBuilder: UrlBuilder,
        searchObjects: SearchObjects,
        analytics: Analytics,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        fieldParser: FieldParser,
        storeOfObjectTypes: StoreOfObjectTypes,
        spaceViews: SpaceViewSubscriptionContainer
    ): LinkToObjectViewModelFactory = LinkToObjectViewModelFactory(
        vmParams = vmParams,
        urlBuilder = urlBuilder,
        searchObjects = searchObjects,
        analytics = analytics,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        fieldParser = fieldParser,
        storeOfObjectTypes = storeOfObjectTypes,
        spaceViews = spaceViews
    )
}
