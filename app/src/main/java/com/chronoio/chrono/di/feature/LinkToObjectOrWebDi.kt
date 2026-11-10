package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.core_utils.tools.UrlValidator
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.editor.Editor
import com.chronoio.chrono.presentation.linking.LinkToObjectOrWebViewModel
import com.chronoio.chrono.presentation.linking.LinkToObjectOrWebViewModelFactory
import com.chronoio.chrono.ui.linking.LinkToObjectOrWebPagesFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(
    modules = [LinkToObjectOrWebModule::class]
)
@PerModal
interface LinkToObjectOrWebSubComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: LinkToObjectOrWebViewModel.VmParams): Builder
        fun module(module: LinkToObjectOrWebModule): Builder
        fun build(): LinkToObjectOrWebSubComponent
    }

    fun inject(fragment: LinkToObjectOrWebPagesFragment)
}

@Module
object LinkToObjectOrWebModule {

    @JvmStatic
    @PerModal
    @Provides
    fun provideLinkToObjectViewModelFactory(
        vmParams: LinkToObjectOrWebViewModel.VmParams,
        urlBuilder: UrlBuilder,
        storeOfObjectTypes: StoreOfObjectTypes,
        searchObjects: SearchObjects,
        analytics: Analytics,
        stores: Editor.Storage,
        urlValidator: UrlValidator,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        fieldParser: FieldParser,
        spaceViews: SpaceViewSubscriptionContainer
    ): LinkToObjectOrWebViewModelFactory = LinkToObjectOrWebViewModelFactory(
        vmParams = vmParams,
        urlBuilder = urlBuilder,
        storeOfObjectTypes = storeOfObjectTypes,
        searchObjects = searchObjects,
        analytics = analytics,
        stores = stores,
        urlValidator = urlValidator,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        fieldParser = fieldParser,
        spaceViews = spaceViews
    )
}