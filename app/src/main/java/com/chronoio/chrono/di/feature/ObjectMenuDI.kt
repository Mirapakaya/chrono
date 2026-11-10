package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerDialog
import com.chronoio.chrono.core_utils.tools.FeatureToggles
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.interactor.CreateBlock
import com.chronoio.chrono.domain.block.interactor.UpdateFields
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.collections.AddObjectToCollection
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.dashboard.interactor.SetObjectListIsFavorite
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.favorites.AddPersonalFavorite
import com.chronoio.chrono.presentation.widgets.ObservePersonalFavoriteTargets
import com.chronoio.chrono.domain.favorites.RemovePersonalFavorite
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.GetSpaceInviteLink
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.DuplicateObject
import com.chronoio.chrono.domain.`object`.GetObject
import com.chronoio.chrono.domain.`object`.SetObjectDetails
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.page.AddBackLinkToObject
import com.chronoio.chrono.domain.page.CloseObject
import com.chronoio.chrono.domain.page.OpenPage
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.relations.AddToFeaturedRelations
import com.chronoio.chrono.domain.relations.DeleteRelationFromObject
import com.chronoio.chrono.domain.relations.RemoveFromFeaturedRelations
import com.chronoio.chrono.domain.templates.CreateTemplateFromObject
import com.chronoio.chrono.domain.widgets.CreateWidget
import com.chronoio.chrono.domain.widgets.DeleteWidget
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.domain.workspace.spaceIdOrNull
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.common.Action
import com.chronoio.chrono.presentation.common.Delegator
import com.chronoio.chrono.presentation.common.PayloadDelegator
import com.chronoio.chrono.presentation.editor.Editor
import com.chronoio.chrono.presentation.objects.menu.ObjectMenuOptionsProvider
import com.chronoio.chrono.presentation.objects.menu.ObjectMenuOptionsProviderImpl
import com.chronoio.chrono.presentation.objects.menu.ObjectMenuViewModel
import com.chronoio.chrono.presentation.objects.menu.ObjectSetMenuViewModel
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.util.downloader.DebugGoroutinesShareDownloader
import com.chronoio.chrono.presentation.util.downloader.DebugTreeShareDownloader
import com.chronoio.chrono.ui.editor.sheets.ObjectMenuFragment
import com.chronoio.chrono.ui.sets.ObjectSetMenuFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull


@Subcomponent(modules = [ObjectMenuModuleBase::class, ObjectMenuModule::class])
@PerDialog
interface ObjectMenuComponent {
    @Subcomponent.Builder
    interface Builder {
        fun base(module: ObjectMenuModuleBase): Builder
        fun module(module: ObjectMenuModule): Builder
        fun build(): ObjectMenuComponent
    }

    fun inject(fragment: ObjectMenuFragment)
}

@Subcomponent(modules = [ObjectMenuModuleBase::class, ObjectSetMenuModule::class])
@PerDialog
interface ObjectSetMenuComponent {
    @Subcomponent.Builder
    interface Builder {
        fun base(module: ObjectMenuModuleBase): Builder
        fun module(module: ObjectSetMenuModule): Builder
        fun build(): ObjectSetMenuComponent
    }

    fun inject(fragment: ObjectSetMenuFragment)
}

@Module
object ObjectMenuModuleBase {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideAddBackLinkToObject(
        openPage: OpenPage,
        createBlock: CreateBlock,
        closeObject: CloseObject,
        dispatchers: AppCoroutineDispatchers
    ): AddBackLinkToObject = AddBackLinkToObject(openPage, createBlock, closeObject, dispatchers)
}

@Module
object ObjectMenuModule {
    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        duplicateObject: DuplicateObject,
        debugTreeShareDownloader: DebugTreeShareDownloader,
        addBackLinkToObject: AddBackLinkToObject,
        urlBuilder: UrlBuilder,
        storage: Editor.Storage,
        analytics: Analytics,
        dispatcher: Dispatcher<Payload>,
        updateFields: UpdateFields,
        featureToggles: FeatureToggles,
        delegator: Delegator<Action>,
        addObjectToCollection: AddObjectToCollection,
        createTemplateFromObject: CreateTemplateFromObject,
        setObjectDetails: SetObjectDetails,
        debugGoroutinesShareDownloader: DebugGoroutinesShareDownloader,
        createWidget: CreateWidget,
        spaceManager: SpaceManager,
        deepLinkResolver: DeepLinkResolver,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        payloadDelegator: PayloadDelegator,
        setObjectListIsFavorite: SetObjectListIsFavorite,
        setObjectIsArchived: SetObjectListIsArchived,
        fieldParser: FieldParser,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
        getSpaceInviteLink: GetSpaceInviteLink,
        addToFeaturedRelations: AddToFeaturedRelations,
        removeFromFeaturedRelations: RemoveFromFeaturedRelations,
        userPermissionProvider: UserPermissionProvider,
        deleteRelationFromObject: DeleteRelationFromObject,
        objectMenuOptionsProvider: ObjectMenuOptionsProvider,
        showObject: GetObject,
        deleteWidget: DeleteWidget,
        addPersonalFavorite: AddPersonalFavorite,
        removePersonalFavorite: RemovePersonalFavorite
    ): ObjectMenuViewModel.Factory = ObjectMenuViewModel.Factory(
        setObjectIsArchived = setObjectIsArchived,
        duplicateObject = duplicateObject,
        debugTreeShareDownloader = debugTreeShareDownloader,
        addBackLinkToObject = addBackLinkToObject,
        urlBuilder = urlBuilder,
        storage = storage,
        analytics = analytics,
        dispatcher = dispatcher,
        updateFields = updateFields,
        delegator = delegator,
        menuOptionsProvider = objectMenuOptionsProvider,
        addObjectToCollection = addObjectToCollection,
        createTemplateFromObject = createTemplateFromObject,
        setObjectDetails = setObjectDetails,
        debugGoroutinesShareDownloader = debugGoroutinesShareDownloader,
        createWidget = createWidget,
        spaceManager = spaceManager,
        deepLinkResolver = deepLinkResolver,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        payloadDelegator = payloadDelegator,
        setObjectListIsFavorite = setObjectListIsFavorite,
        fieldParser = fieldParser,
        getSpaceInviteLink = getSpaceInviteLink,
        spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
        addToFeaturedRelations = addToFeaturedRelations,
        removeFromFeaturedRelations = removeFromFeaturedRelations,
        userPermissionProvider = userPermissionProvider,
        deleteRelationFromObject = deleteRelationFromObject,
        showObject = showObject,
        deleteWidget = deleteWidget,
        addPersonalFavorite = addPersonalFavorite,
        removePersonalFavorite = removePersonalFavorite
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideDeleteRelationFromObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteRelationFromObject = DeleteRelationFromObject(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideSetObjectDetails(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(
        repo,
        dispatchers
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @PerDialog
    @JvmStatic
    fun provideMenuOptionsProvider(
        storage: Editor.Storage,
        spaceManager: SpaceManager,
        observePersonalFavoriteTargets: ObservePersonalFavoriteTargets,
        userPermissionProvider: UserPermissionProvider
    ): ObjectMenuOptionsProvider {
        // From the state, not the config flow: a space can be active without a config
        // (SpaceManager.activate, used by quick capture to skip Workspace.Open), and the
        // config flow stays silent in that case — which would leave this menu, opened from
        // the quick-capture sheet, with no space at all.
        val spaceIdFlow = spaceManager.state()
            .mapNotNull { it.spaceIdOrNull() }
            .distinctUntilChanged()
        val personalFavoriteTargets = spaceIdFlow.flatMapLatest { space ->
            observePersonalFavoriteTargets(space).map { it.toSet() }
        }
        val canToggleChannelPin = spaceIdFlow.flatMapLatest { space ->
            userPermissionProvider.observe(space).map { it?.isOwnerOrEditor() == true }
        }
        return ObjectMenuOptionsProviderImpl(
            objectViewDetailsFlow = storage.details.stream(),
            hasObjectLayoutConflict = storage.hasLayoutOrRelationConflict.stream(),
            personalFavoriteTargets = personalFavoriteTargets,
            canToggleChannelPin = canToggleChannelPin
        )
    }

    @JvmStatic
    @Provides
    @PerDialog
    fun provideAddObjectToCollection(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): AddObjectToCollection = AddObjectToCollection(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideFavoriteUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsFavorite = SetObjectListIsFavorite(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerDialog
    fun provideArchiveUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerDialog
    fun addToFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): AddToFeaturedRelations =
        AddToFeaturedRelations(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerDialog
    fun removeFromFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): RemoveFromFeaturedRelations =
        RemoveFromFeaturedRelations(repo, dispatchers)
}

@Module
object ObjectSetMenuModule {

    @JvmStatic
    @Provides
    @PerDialog
    fun provideUpdateFieldsUseCase(
        repo: BlockRepository
    ): UpdateFields = UpdateFields(repo)

    @JvmStatic
    @Provides
    @PerDialog
    fun provideViewModelFactory(
        addBackLinkToObject: AddBackLinkToObject,
        duplicateObject: DuplicateObject,
        delegator: Delegator<Action>,
        urlBuilder: UrlBuilder,
        analytics: Analytics,
        state: MutableStateFlow<ObjectState>,
        updateFields: UpdateFields,
        featureToggles: FeatureToggles,
        dispatcher: Dispatcher<Payload>,
        addObjectToCollection: AddObjectToCollection,
        debugGoroutinesShareDownloader: DebugGoroutinesShareDownloader,
        createWidget: CreateWidget,
        spaceManager: SpaceManager,
        deepLinkResolver: DeepLinkResolver,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        payloadDelegator: PayloadDelegator,
        setObjectListIsFavorite: SetObjectListIsFavorite,
        setObjectIsArchived: SetObjectListIsArchived,
        fieldParser: FieldParser,
        getSpaceInviteLink: GetSpaceInviteLink,
        spaceViewSubscriptionContainer: SpaceViewSubscriptionContainer,
        addToFeaturedRelations: AddToFeaturedRelations,
        removeFromFeaturedRelations: RemoveFromFeaturedRelations,
        userPermissionProvider: UserPermissionProvider,
        deleteRelationFromObject: DeleteRelationFromObject,
        setObjectDetails: SetObjectDetails,
        objectMenuOptionsProvider: ObjectMenuOptionsProvider,
        showObject: GetObject,
        deleteWidget: DeleteWidget,
        addPersonalFavorite: AddPersonalFavorite,
        removePersonalFavorite: RemovePersonalFavorite
    ): ObjectSetMenuViewModel.Factory = ObjectSetMenuViewModel.Factory(
        setObjectListIsArchived = setObjectIsArchived,
        addBackLinkToObject = addBackLinkToObject,
        duplicateObject = duplicateObject,
        urlBuilder = urlBuilder,
        delegator = delegator,
        analytics = analytics,
        objectState = state,
        dispatcher = dispatcher,
        menuOptionsProvider = objectMenuOptionsProvider,
        addObjectToCollection = addObjectToCollection,
        debugGoroutinesShareDownloader = debugGoroutinesShareDownloader,
        createWidget = createWidget,
        spaceManager = spaceManager,
        deepLinkResolver = deepLinkResolver,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        payloadDelegator = payloadDelegator,
        setObjectListIsFavorite = setObjectListIsFavorite,
        fieldParser = fieldParser,
        getSpaceInviteLink = getSpaceInviteLink,
        spaceViewSubscriptionContainer = spaceViewSubscriptionContainer,
        addToFeaturedRelations = addToFeaturedRelations,
        removeFromFeaturedRelations = removeFromFeaturedRelations,
        userPermissionProvider = userPermissionProvider,
        deleteRelationFromObject = deleteRelationFromObject,
        updateFields = updateFields,
        setObjectDetails = setObjectDetails,
        showObject = showObject,
        deleteWidget = deleteWidget,
        addPersonalFavorite = addPersonalFavorite,
        removePersonalFavorite = removePersonalFavorite
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideDeleteRelationFromObject(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteRelationFromObject = DeleteRelationFromObject(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideOpenPage(
        repo: BlockRepository,
        settings: UserSettingsRepository,
        dispatchers: AppCoroutineDispatchers
    ): OpenPage = OpenPage(
        repo = repo,
        settings = settings,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun provideFavoriteUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsFavorite = SetObjectListIsFavorite(repo = repo, dispatchers = dispatchers)

    @OptIn(ExperimentalCoroutinesApi::class)
    @JvmStatic
    @Provides
    @PerDialog
    fun provideMenuOptionsProvider(
        state: MutableStateFlow<ObjectState>,
        spaceManager: SpaceManager,
        observePersonalFavoriteTargets: ObservePersonalFavoriteTargets,
        userPermissionProvider: UserPermissionProvider
    ): ObjectMenuOptionsProvider {
        val objectViewDetailsFlow = state
            .flatMapLatest { currentState ->
                if (currentState is ObjectState.DataView) {
                    flowOf(currentState.details)
                } else {
                    emptyFlow()
                }
            }
            .distinctUntilChanged()

        val hasObjectLayoutConflictFlow = state
            .flatMapLatest { currentState ->
                if (currentState is ObjectState.DataView) {
                    flowOf(currentState.hasObjectLayoutConflict)
                } else {
                    emptyFlow()
                }
            }
            .distinctUntilChanged()

        // From the state, not the config flow: a space can be active without a config
        // (SpaceManager.activate, used by quick capture to skip Workspace.Open), and the
        // config flow stays silent in that case — which would leave this menu, opened from
        // the quick-capture sheet, with no space at all.
        val spaceIdFlow = spaceManager.state()
            .mapNotNull { it.spaceIdOrNull() }
            .distinctUntilChanged()
        val personalFavoriteTargets = spaceIdFlow.flatMapLatest { space ->
            observePersonalFavoriteTargets(space).map { it.toSet() }
        }
        val canToggleChannelPin = spaceIdFlow.flatMapLatest { space ->
            userPermissionProvider.observe(space).map { it?.isOwnerOrEditor() == true }
        }

        return ObjectMenuOptionsProviderImpl(
            objectViewDetailsFlow = objectViewDetailsFlow,
            hasObjectLayoutConflict = hasObjectLayoutConflictFlow,
            personalFavoriteTargets = personalFavoriteTargets,
            canToggleChannelPin = canToggleChannelPin
        )
    }

    @JvmStatic
    @Provides
    @PerDialog
    fun provideSetObjectDetails(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(
        repo,
        dispatchers
    )

    @JvmStatic
    @Provides
    @PerDialog
    fun addToFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): AddToFeaturedRelations =
        AddToFeaturedRelations(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerDialog
    fun removeFromFeaturedRelations(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): RemoveFromFeaturedRelations =
        RemoveFromFeaturedRelations(repo, dispatchers)
}