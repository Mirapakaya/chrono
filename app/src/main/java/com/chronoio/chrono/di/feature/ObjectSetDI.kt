package com.chronoio.chrono.di.feature

import android.content.Context
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.primitives.Space
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.feature.cover.UnsplashSubComponent
import com.chronoio.chrono.di.feature.relations.RelationAddToDataViewSubComponent
import com.chronoio.chrono.di.feature.relations.RelationAddToObjectSubComponent
import com.chronoio.chrono.di.feature.relations.RelationCreateFromScratchForDataViewSubComponent
import com.chronoio.chrono.di.feature.relations.RelationCreateFromScratchForObjectSubComponent
import com.chronoio.chrono.di.feature.sets.CreateFilterSubComponent
import com.chronoio.chrono.di.feature.sets.ModifyFilterSubComponent
import com.chronoio.chrono.di.feature.sets.SelectFilterRelationSubComponent
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.interactor.CreateBlock
import com.chronoio.chrono.domain.block.interactor.UpdateText
import com.chronoio.chrono.domain.block.interactor.sets.GetObjectTypes
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.collections.AddObjectToCollection
import com.chronoio.chrono.domain.collections.RemoveObjectFromCollection
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.cover.SetDocCoverImage
import com.chronoio.chrono.domain.dataview.SetDataViewProperties
import com.chronoio.chrono.domain.discussions.AddDiscussion
import com.chronoio.chrono.domain.dataview.interactor.AddDataViewViewer
import com.chronoio.chrono.domain.dataview.interactor.CreateDataViewObject
import com.chronoio.chrono.domain.dataview.interactor.DeleteDataViewViewer
import com.chronoio.chrono.domain.dataview.interactor.DuplicateDataViewViewer
import com.chronoio.chrono.domain.dataview.interactor.SetDataViewObjectOrder
import com.chronoio.chrono.domain.dataview.interactor.SetDataViewViewerPosition
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.domain.event.interactor.EventChannel
import com.chronoio.chrono.domain.event.interactor.InterceptEvents
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.icon.SetDocumentImageIcon
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.ConvertObjectToCollection
import com.chronoio.chrono.domain.`object`.DuplicateObject
import com.chronoio.chrono.domain.`object`.DuplicateObjects
import com.chronoio.chrono.domain.`object`.UpdateDetail
import com.chronoio.chrono.domain.objects.DefaultObjectStore
import com.chronoio.chrono.domain.objects.ObjectStore
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.objects.options.GetOptions
import com.chronoio.chrono.domain.page.CloseObject
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.relations.AddFileToObject
import com.chronoio.chrono.domain.relations.AddRelationToObject
import com.chronoio.chrono.domain.relations.DeleteRelationFromDataView
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.CancelSearchSubscription
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.search.BoardGroupSubscriptionContainer
import com.chronoio.chrono.domain.search.BoardRecordsSubscriptionContainer
import com.chronoio.chrono.domain.search.DataViewSubscriptionContainer
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.search.SubscriptionEventChannel
import com.chronoio.chrono.domain.sets.OpenObjectSet
import com.chronoio.chrono.domain.sets.SetQueryToObjectSet
import com.chronoio.chrono.domain.spaces.ClearLastOpenedSpace
import com.chronoio.chrono.domain.templates.CreateTemplate
import com.chronoio.chrono.domain.templates.GetTemplates
import com.chronoio.chrono.domain.unsplash.DownloadUnsplashImage
import com.chronoio.chrono.domain.unsplash.UnsplashRepository
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.emojifier.data.Emoji
import com.chronoio.chrono.emojifier.data.EmojiProvider
import com.chronoio.chrono.emojifier.suggest.EmojiSuggester
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.common.Action
import com.chronoio.chrono.presentation.common.Delegator
import com.chronoio.chrono.presentation.editor.cover.CoverImageHashProvider
import com.chronoio.chrono.presentation.objects.LockedStateProvider
import com.chronoio.chrono.presentation.relations.providers.DataViewObjectRelationProvider
import com.chronoio.chrono.presentation.relations.providers.DataViewObjectValueProvider
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationListProvider
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationProvider
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationProvider.Companion.DATA_VIEW_PROVIDER_TYPE
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationProvider.Companion.INTRINSIC_PROVIDER_TYPE
import com.chronoio.chrono.presentation.relations.providers.ObjectValueProvider
import com.chronoio.chrono.presentation.relations.providers.SetOrCollectionObjectValueProvider
import com.chronoio.chrono.presentation.relations.providers.SetOrCollectionRelationProvider
import com.chronoio.chrono.presentation.sets.ObjectSetDatabase
import com.chronoio.chrono.presentation.sets.ObjectSetPaginator
import com.chronoio.chrono.presentation.sets.ObjectSetSession
import com.chronoio.chrono.presentation.sets.ObjectSetViewModel
import com.chronoio.chrono.presentation.navigation.backstack.BackHistoryDelegate
import com.chronoio.chrono.presentation.navigation.backstack.NavigationBackStackInspector
import com.chronoio.chrono.presentation.sets.ObjectSetViewModelFactory
import com.chronoio.chrono.presentation.vault.ExitToVaultDelegate
import com.chronoio.chrono.presentation.sets.state.DefaultObjectStateReducer
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.sets.state.ObjectStateReducer
import com.chronoio.chrono.presentation.sets.subscription.DataViewSubscription
import com.chronoio.chrono.presentation.sets.subscription.DefaultDataViewSubscription
import com.chronoio.chrono.presentation.sets.viewer.DefaultViewerDelegate
import com.chronoio.chrono.presentation.sets.viewer.ViewerDelegate
import com.chronoio.chrono.presentation.templates.ObjectTypeTemplatesContainer
import com.chronoio.chrono.presentation.util.CopyFileToCacheDirectory
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.util.defaultCopyFileToCacheDirectory
import com.chronoio.chrono.providers.DefaultCoverImageHashProvider
import com.chronoio.chrono.ui.sets.ObjectSetFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow

@Subcomponent(modules = [ObjectSetModule::class])
@PerScreen
interface ObjectSetSubComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withParams(param: ObjectSetViewModel.Params) : Builder
        fun module(module: ObjectSetModule): Builder
        fun build(): ObjectSetSubComponent
    }

    fun inject(fragment: ObjectSetFragment)

    fun objectSetRecordComponent(): ObjectSetRecordSubComponent.Builder
    fun objectSetCreateBookmarkRecordComponent(): ObjectSetCreateBookmarkRecordSubComponent.Builder
    fun viewerFilterBySubComponent(): ViewerFilterSubComponent.Builder

    fun objectSetSettingsComponent(): ObjectSetSettingsSubComponent.Builder
    fun relationAddToDataViewComponent(): RelationAddToDataViewSubComponent.Builder
    fun relationCreateFromScratchForDataViewComponent(): RelationCreateFromScratchForDataViewSubComponent.Builder
    fun selectSortRelationComponent(): SelectSortRelationSubComponent.Builder
    fun selectFilterRelationComponent(): SelectFilterRelationSubComponent.Builder
    fun createFilterComponent(): CreateFilterSubComponent.Builder
    fun modifyFilterComponent(): ModifyFilterSubComponent.Builder
    fun viewerSortComponent(): ViewerSortSubComponent.Builder
    fun modifyViewerSortComponent(): ModifyViewerSortSubComponent.Builder

    fun relationTextValueComponent(): RelationTextValueSubComponent.Builder
    fun relationDataViewTextValueComponent(): RelationDataViewTextValueSubComponent.Builder

    fun relationDateValueComponent(): DefaultRelationDataValueSubComponent.Builder
    fun dataViewRelationDateValueComponent(): DataViewRelationDataValueSubComponent.Builder

    fun objectSetMenuComponent(): ObjectSetMenuComponent.Builder
    fun objectSetIconPickerComponent(): ObjectSetIconPickerComponent.Builder
    fun objectSetCoverComponent(): SelectCoverObjectSetSubComponent.Builder
    fun objectUnsplashComponent(): UnsplashSubComponent.Builder

    fun objectRelationListComponent(): ObjectRelationListComponent.Builder
    fun relationAddToObjectComponent(): RelationAddToObjectSubComponent.Builder
    fun relationCreateFromScratchForObjectComponent(): RelationCreateFromScratchForObjectSubComponent.Builder

    fun tagStatusSetComponent(): TagOrStatusValueSetComponent.Builder
    fun tagStatusDataViewComponent(): TagOrStatusValueDataViewComponent.Builder
    fun optionSetComponent(): CreateOrEditOptionSetComponent.Builder
    fun optionDataViewComponent(): CreateOrEditOptionDataViewComponent.Builder
    fun objectValueSetComponent(): ObjectValueSetComponent.Builder
    fun objectValueDataViewComponent(): ObjectValueDataViewComponent.Builder
}

@Module(
    includes = [ObjectSetModule.Bindings::class]
)
object ObjectSetModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDocumentImageIconUseCase(
        repo: BlockRepository
    ): SetDocumentImageIcon = SetDocumentImageIcon(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDataViewSubscription(
        dataViewSubscriptionContainer: DataViewSubscriptionContainer
    ): DataViewSubscription = DefaultDataViewSubscription(
        dataViewSubscriptionContainer
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDataViewProperties(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetDataViewProperties = SetDataViewProperties(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDuplicateObjectsListUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DuplicateObjects = DuplicateObjects(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectSetViewModelFactory(
        params: ObjectSetViewModel.Params,
        openObjectSet: OpenObjectSet,
        closeObject: CloseObject,
        setObjectDetails: UpdateDetail,
        updateText: UpdateText,
        createBlock: CreateBlock,
        interceptEvents: InterceptEvents,
        createDataViewObject: CreateDataViewObject,
        createObject: CreateObject,
        dispatcher: Dispatcher<Payload>,
        delegator: Delegator<Action>,
        urlBuilder: UrlBuilder,
        coverImageHashProvider: CoverImageHashProvider,
        session: ObjectSetSession,
        analytics: Analytics,
        downloadUnsplashImage: DownloadUnsplashImage,
        setDocCoverImage: SetDocCoverImage,
        dataViewSubscriptionContainer: DataViewSubscriptionContainer,
        setQueryToObjectSet: SetQueryToObjectSet,
        database: ObjectSetDatabase,
        paginator: ObjectSetPaginator,
        storeOfRelations: StoreOfRelations,
        objectStateReducer: ObjectStateReducer,
        dataViewSubscription: DataViewSubscription,
        @Named("object-set-store") objectStore: ObjectStore,
        addObjectToCollection: AddObjectToCollection,
        removeObjectFromCollection: RemoveObjectFromCollection,
        convertObjectToCollection: ConvertObjectToCollection,
        storeOfObjectTypes: StoreOfObjectTypes,
        getObjectTypes: GetObjectTypes,
        duplicateObjects: DuplicateObjects,
        templatesContainer: ObjectTypeTemplatesContainer,
        setObjectListIsArchived: SetObjectListIsArchived,
        createTemplate: CreateTemplate,
        viewerDelegate: ViewerDelegate,
        spaceManager: SpaceManager,
        dateProvider: DateProvider,
        permissions: UserPermissionProvider,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider,
        fieldParser: FieldParser,
        spaceViews: SpaceViewSubscriptionContainer,
        deepLinkResolver: DeepLinkResolver,
        setDataViewProperties: SetDataViewProperties,
        setDataViewObjectOrder: SetDataViewObjectOrder,
        boardGroupSubscriptionContainer: BoardGroupSubscriptionContainer,
        boardRecordsSubscriptionContainer: BoardRecordsSubscriptionContainer,
        storelessSubscriptionContainer: StorelessSubscriptionContainer,
        emojiProvider: EmojiProvider,
        emojiSuggester: EmojiSuggester,
        stringResourceProvider: StringResourceProvider,
        getDefaultObjectType: GetDefaultObjectType,
        addDiscussion: AddDiscussion,
        userSettingsRepository: UserSettingsRepository,
        backHistoryDelegate: BackHistoryDelegate,
        exitToVaultDelegate: ExitToVaultDelegate
    ): ObjectSetViewModelFactory = ObjectSetViewModelFactory(
        params = params,
        openObjectSet = openObjectSet,
        closeObject = closeObject,
        setObjectDetails = setObjectDetails,
        createDataViewObject = createDataViewObject,
        updateText = updateText,
        createBlock = createBlock,
        interceptEvents = interceptEvents,
        dispatcher = dispatcher,
        delegator = delegator,
        coverImageHashProvider = coverImageHashProvider,
        urlBuilder = urlBuilder,
        session = session,
        analytics = analytics,
        downloadUnsplashImage = downloadUnsplashImage,
        setDocCoverImage = setDocCoverImage,
        createObject = createObject,
        dataViewSubscriptionContainer = dataViewSubscriptionContainer,
        setQueryToObjectSet = setQueryToObjectSet,
        database = database,
        paginator = paginator,
        storeOfRelations = storeOfRelations,
        objectStateReducer = objectStateReducer,
        dataViewSubscription = dataViewSubscription,
        objectStore = objectStore,
        addObjectToCollection = addObjectToCollection,
        objectToCollection = convertObjectToCollection,
        storeOfObjectTypes = storeOfObjectTypes,
        duplicateObjects = duplicateObjects,
        templatesContainer = templatesContainer,
        setObjectListIsArchived = setObjectListIsArchived,
        viewerDelegate = viewerDelegate,
        spaceManager = spaceManager,
        createTemplate = createTemplate,
        dateProvider = dateProvider,
        permissions = permissions,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        spaceSyncAndP2PStatusProvider = spaceSyncAndP2PStatusProvider,
        fieldParser = fieldParser,
        spaceViews = spaceViews,
        deepLinkResolver = deepLinkResolver,
        removeObjectFromCollection = removeObjectFromCollection,
        setDataViewProperties = setDataViewProperties,
        setDataViewObjectOrder = setDataViewObjectOrder,
        boardGroupSubscriptionContainer = boardGroupSubscriptionContainer,
        boardRecordsSubscriptionContainer = boardRecordsSubscriptionContainer,
        storelessSubscriptionContainer = storelessSubscriptionContainer,
        emojiProvider = emojiProvider,
        emojiSuggester = emojiSuggester,
        stringResourceProvider = stringResourceProvider,
        getDefaultObjectType = getDefaultObjectType,
        addDiscussion = addDiscussion,
        userSettingsRepository = userSettingsRepository,
        backHistoryDelegate = backHistoryDelegate,
        exitToVaultDelegate = exitToVaultDelegate
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBackHistoryDelegate(
        inspector: NavigationBackStackInspector,
        searchObjects: SearchObjects,
        fieldParser: FieldParser,
        dispatchers: AppCoroutineDispatchers
    ): BackHistoryDelegate = BackHistoryDelegate.Default(
        inspector = inspector,
        searchObjects = searchObjects,
        fieldParser = fieldParser,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideExitToVaultDelegate(
        spaceManager: SpaceManager,
        clearLastOpenedSpace: ClearLastOpenedSpace
    ): ExitToVaultDelegate = ExitToVaultDelegate.Default(
        spaceManager = spaceManager,
        clearLastOpenedSpace = clearLastOpenedSpace
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideConvertObjectToCollection(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): ConvertObjectToCollection = ConvertObjectToCollection(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateBlock(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreateBlock = CreateBlock(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun getCreateObject(
        repo: BlockRepository,
        getDefaultObjectType: GetDefaultObjectType,
        dispatchers: AppCoroutineDispatchers
    ): CreateObject = CreateObject(
        repo = repo,
        getDefaultObjectType = getDefaultObjectType,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetQueryToSet(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetQueryToObjectSet = SetQueryToObjectSet(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun provideGetDefaultPageType(
        userSettingsRepository: UserSettingsRepository,
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = userSettingsRepository,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideOpenObjectSetUseCase(
        repo: BlockRepository,
        settings: UserSettingsRepository
    ): OpenObjectSet = OpenObjectSet(
        repo = repo,
        settings = settings
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateTextUseCase(
        repo: BlockRepository
    ): UpdateText = UpdateText(repo = repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideInterceptEventsUseCase(
        channel: EventChannel
    ): InterceptEvents = InterceptEvents(
        channel = channel,
        context = Dispatchers.IO
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideClosePageUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CloseObject = CloseObject(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideState(reducer: ObjectStateReducer): MutableStateFlow<ObjectState> = reducer.state

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectStateReducer(): ObjectStateReducer = DefaultObjectStateReducer()

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectSetSession(): ObjectSetSession = ObjectSetSession()

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDelegator(): Delegator<Action> = Delegator.Default()

    @JvmStatic
    @Provides
    @PerScreen
    @Named(DATA_VIEW_PROVIDER_TYPE)
    fun provideDataViewObjectRelationProvider(
        state: MutableStateFlow<ObjectState>,
        storeOfRelations: StoreOfRelations
    ): ObjectRelationProvider = DataViewObjectRelationProvider(
        objectState = state,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @PerScreen
    @Named(INTRINSIC_PROVIDER_TYPE)
    fun provideObjectRelationProvider(
        state: MutableStateFlow<ObjectState>,
        storeOfRelations: StoreOfRelations
    ): ObjectRelationProvider = SetOrCollectionRelationProvider(
        objectState = state,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @PerScreen
    @Named(DATA_VIEW_PROVIDER_TYPE)
    fun dataViewObjectValueProvider(
        db: ObjectSetDatabase,
        objectState: MutableStateFlow<ObjectState>,
    ): ObjectValueProvider = DataViewObjectValueProvider(
        objectState = objectState,
        db = db
    )

    @JvmStatic
    @Provides
    @PerScreen
    @Named(INTRINSIC_PROVIDER_TYPE)
    fun setOrCollectionObjectValueProvider(
        db: ObjectSetDatabase,
        objectState: MutableStateFlow<ObjectState>,
    ): ObjectValueProvider = SetOrCollectionObjectValueProvider(
        objectState = objectState,
        db = db
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateDetailUseCase(
        repository: BlockRepository
    ): UpdateDetail = UpdateDetail(repository)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDeleteRelationFromDataViewUseCase(
        repo: BlockRepository
    ): DeleteRelationFromDataView = DeleteRelationFromDataView(repo = repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDocCoverImageUseCase(
        repo: BlockRepository
    ): SetDocCoverImage = SetDocCoverImage(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDownload(repo: UnsplashRepository): DownloadUnsplashImage = DownloadUnsplashImage(
        repo = repo
    )

    @JvmStatic
    @PerScreen
    @Provides
    fun getSetObjectListIsArchived(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetTemplates(
        repo: BlockRepository,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers
    ): GetTemplates = GetTemplates(
        repo = repo,
        spaceManager = spaceManager,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun objectSearchSubscriptionContainer(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        channel: SubscriptionEventChannel,
        @Named("object-set-store") store: ObjectStore
    ): DataViewSubscriptionContainer = DataViewSubscriptionContainer(
        repo = repo,
        channel = channel,
        store = store,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun boardGroupSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): BoardGroupSubscriptionContainer = BoardGroupSubscriptionContainer(
        repo = repo,
        channel = channel,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun getOptions(repo: BlockRepository) = GetOptions(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun boardRecordsSubscriptionContainer(
        repo: BlockRepository,
        channel: SubscriptionEventChannel,
        @Named("object-set-store") store: ObjectStore,
        dispatchers: AppCoroutineDispatchers,
        logger: Logger
    ): BoardRecordsSubscriptionContainer = BoardRecordsSubscriptionContainer(
        repo = repo,
        channel = channel,
        store = store,
        dispatchers = dispatchers,
        logger = logger
    )

    @JvmStatic
    @Provides
    @PerScreen
    @Named("object-set-store")
    fun provideObjectStore(): ObjectStore = DefaultObjectStore()

    @JvmStatic
    @Provides
    @PerScreen
    fun cancelSearchSubscription(
        repo: BlockRepository,
        @Named("object-set-store") store: ObjectStore
    ): CancelSearchSubscription = CancelSearchSubscription(
        repo = repo,
        store = store
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectSetDatabase(
        @Named("object-set-store") store: ObjectStore
    ): ObjectSetDatabase = ObjectSetDatabase(
        store = store
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun providePaginator(): ObjectSetPaginator = ObjectSetPaginator()

    @JvmStatic
    @Provides
    @PerScreen
    fun provideAddFileToObjectUseCase(
        repo: BlockRepository
    ): AddFileToObject = AddFileToObject(repo = repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun duplicateObject(
        repo: BlockRepository
    ): DuplicateObject = DuplicateObject(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideAddObjectToCollection(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): AddObjectToCollection = AddObjectToCollection(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRemoveObjectToCollection(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): RemoveObjectFromCollection = RemoveObjectFromCollection(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun lockedStateProvider() : LockedStateProvider = LockedStateProvider.DataViewLockedStateProvider

    @JvmStatic
    @Provides
    @PerScreen
    fun dataViewRelationListProvider(
        objectStateFlow: MutableStateFlow<ObjectState>
    ) : ObjectRelationListProvider = ObjectRelationListProvider.ObjectSetRelationListProvider(
        objectStates = objectStateFlow
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideAddRelationToObject(repo: BlockRepository, dispatchers: AppCoroutineDispatchers)
            : AddRelationToObject = AddRelationToObject(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCopyFileToCache(
        context: Context
    ): CopyFileToCacheDirectory = defaultCopyFileToCacheDirectory(context)

    @Module
    interface Bindings {

        @PerScreen
        @Binds
        fun bindCoverImageHashProvider(
            defaultProvider: DefaultCoverImageHashProvider
        ): CoverImageHashProvider
    }

    @JvmStatic
    @Provides
    @PerScreen
    fun provideAddDataViewViewerUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): AddDataViewViewer = AddDataViewViewer(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRenameDataViewViewerUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): UpdateDataViewViewer = UpdateDataViewViewer(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDuplicateDataViewViewerUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DuplicateDataViewViewer = DuplicateDataViewViewer(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDeleteDataViewViewerUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): DeleteDataViewViewer = DeleteDataViewViewer(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDataViewViewerPositionUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetDataViewViewerPosition = SetDataViewViewerPosition(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDataViewObjectOrderUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetDataViewObjectOrder = SetDataViewObjectOrder(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideViewerDelegate(
        session: ObjectSetSession,
        addDataViewViewer: AddDataViewViewer,
        duplicateDataViewViewer: DuplicateDataViewViewer,
        deleteDataViewViewer: DeleteDataViewViewer,
        setDataViewViewerPosition: SetDataViewViewerPosition,
        analytics: Analytics,
        dispatcher: Dispatcher<Payload>,
        updateDataViewViewer: UpdateDataViewViewer
    ): ViewerDelegate = DefaultViewerDelegate(
        session = session,
        addDataViewViewer = addDataViewViewer,
        updateDataViewViewer = updateDataViewViewer,
        duplicateDataViewViewer = duplicateDataViewViewer,
        deleteDataViewViewer = deleteDataViewViewer,
        setDataViewViewerPosition = setDataViewViewerPosition,
        analytics = analytics,
        dispatcher = dispatcher
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideEmojiProvider(): EmojiProvider = Emoji
}

data class DefaultComponentParam(
    val ctx: Id,
    val space: Space
)

