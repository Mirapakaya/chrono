package com.chronoio.chrono.di.feature

import android.content.Context
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.core_utils.tools.FeatureToggles
import com.chronoio.chrono.di.feature.cover.UnsplashSubComponent
import com.chronoio.chrono.di.feature.history.VersionHistoryComponent
import com.chronoio.chrono.di.feature.relations.RelationAddToObjectSubComponent
import com.chronoio.chrono.di.feature.relations.RelationCreateFromScratchForObjectBlockSubComponent
import com.chronoio.chrono.di.feature.relations.RelationCreateFromScratchForObjectSubComponent
import com.chronoio.chrono.domain.ai.TypeSuggestionEngine
import com.chronoio.chrono.domain.auth.interactor.ClearLastOpenedObject
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.UpdateDivider
import com.chronoio.chrono.domain.block.interactor.ClearBlockContent
import com.chronoio.chrono.domain.block.interactor.ClearBlockStyle
import com.chronoio.chrono.domain.block.interactor.CreateBlock
import com.chronoio.chrono.domain.block.interactor.DuplicateBlock
import com.chronoio.chrono.domain.block.interactor.MergeBlocks
import com.chronoio.chrono.domain.block.interactor.Move
import com.chronoio.chrono.domain.block.interactor.RemoveLinkMark
import com.chronoio.chrono.domain.block.interactor.ReplaceBlock
import com.chronoio.chrono.domain.block.interactor.SetLinkAppearance
import com.chronoio.chrono.domain.block.interactor.SetObjectType
import com.chronoio.chrono.domain.block.interactor.SplitBlock
import com.chronoio.chrono.domain.block.interactor.TurnIntoDocument
import com.chronoio.chrono.domain.block.interactor.TurnIntoStyle
import com.chronoio.chrono.domain.block.interactor.UnlinkBlocks
import com.chronoio.chrono.domain.block.interactor.UpdateAlignment
import com.chronoio.chrono.domain.block.interactor.UpdateBackgroundColor
import com.chronoio.chrono.domain.block.interactor.UpdateBlocksMark
import com.chronoio.chrono.domain.block.interactor.UpdateCheckbox
import com.chronoio.chrono.domain.block.interactor.UpdateFields
import com.chronoio.chrono.domain.block.interactor.UpdateLinkMarks
import com.chronoio.chrono.domain.block.interactor.UpdateText
import com.chronoio.chrono.domain.block.interactor.UpdateTextColor
import com.chronoio.chrono.domain.block.interactor.UpdateTextStyle
import com.chronoio.chrono.domain.block.interactor.UploadBlock
import com.chronoio.chrono.domain.block.interactor.sets.CreateObjectSet
import com.chronoio.chrono.domain.block.interactor.sets.GetObjectTypes
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.clipboard.Clipboard
import com.chronoio.chrono.domain.clipboard.Copy
import com.chronoio.chrono.domain.clipboard.Paste
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.cover.SetDocCoverImage
import com.chronoio.chrono.domain.chats.GetChatMessages
import com.chronoio.chrono.domain.discussions.AddDiscussion
import com.chronoio.chrono.domain.download.DownloadFile
import com.chronoio.chrono.domain.download.Downloader
import com.chronoio.chrono.domain.event.interactor.EventChannel
import com.chronoio.chrono.domain.event.interactor.InterceptEvents
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.icon.SetDocumentImageIcon
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.networkmode.GetNetworkMode
import com.chronoio.chrono.domain.`object`.ConvertObjectToCollection
import com.chronoio.chrono.domain.`object`.ConvertObjectToSet
import com.chronoio.chrono.domain.`object`.DuplicateObject
import com.chronoio.chrono.domain.`object`.UpdateDetail
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.objects.options.GetOptions
import com.chronoio.chrono.domain.page.CloseObject
import com.chronoio.chrono.domain.page.CreateBlockLinkWithObject
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.page.CreateObjectAsMentionOrLink
import com.chronoio.chrono.domain.page.OpenPage
import com.chronoio.chrono.domain.page.Redo
import com.chronoio.chrono.domain.page.Undo
import com.chronoio.chrono.domain.page.UpdateTitle
import com.chronoio.chrono.domain.page.bookmark.CreateBookmarkBlock
import com.chronoio.chrono.domain.page.bookmark.SetupBookmark
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.relations.AddFileToObject
import com.chronoio.chrono.domain.relations.AddRelationToObject
import com.chronoio.chrono.domain.relations.SetRelationKey
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.spaces.ClearLastOpenedSpace
import com.chronoio.chrono.domain.sets.FindObjectSetForType
import com.chronoio.chrono.domain.table.CreateTable
import com.chronoio.chrono.domain.table.CreateTableColumn
import com.chronoio.chrono.domain.table.CreateTableRow
import com.chronoio.chrono.domain.table.DeleteTableColumn
import com.chronoio.chrono.domain.table.DeleteTableRow
import com.chronoio.chrono.domain.table.DuplicateTableColumn
import com.chronoio.chrono.domain.table.DuplicateTableRow
import com.chronoio.chrono.domain.table.FillTableColumn
import com.chronoio.chrono.domain.table.FillTableRow
import com.chronoio.chrono.domain.table.MoveTableColumn
import com.chronoio.chrono.domain.table.MoveTableRow
import com.chronoio.chrono.domain.table.SetTableRowHeader
import com.chronoio.chrono.domain.templates.ApplyTemplate
import com.chronoio.chrono.domain.unsplash.DownloadUnsplashImage
import com.chronoio.chrono.domain.unsplash.UnsplashRepository
import com.chronoio.chrono.domain.workspace.FileLimitsEventChannel
import com.chronoio.chrono.domain.workspace.InterceptFileLimitEvents
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.common.Action
import com.chronoio.chrono.presentation.common.Delegator
import com.chronoio.chrono.presentation.editor.DocumentExternalEventReducer
import com.chronoio.chrono.presentation.editor.Editor
import com.chronoio.chrono.presentation.editor.EditorViewModel
import com.chronoio.chrono.presentation.editor.EditorViewModelFactory
import com.chronoio.chrono.presentation.navigation.backstack.BackHistoryDelegate
import com.chronoio.chrono.presentation.navigation.backstack.NavigationBackStackInspector
import com.chronoio.chrono.presentation.vault.ExitToVaultDelegate
import com.chronoio.chrono.presentation.editor.cover.CoverImageHashProvider
import com.chronoio.chrono.presentation.editor.editor.Interactor
import com.chronoio.chrono.presentation.editor.editor.Orchestrator
import com.chronoio.chrono.presentation.editor.editor.pattern.DefaultPatternMatcher
import com.chronoio.chrono.presentation.editor.editor.table.DefaultEditorTableDelegate
import com.chronoio.chrono.presentation.editor.editor.table.EditorTableDelegate
import com.chronoio.chrono.presentation.editor.render.DefaultBlockViewRenderer
import com.chronoio.chrono.presentation.editor.selection.SelectionStateHolder
import com.chronoio.chrono.presentation.editor.toggle.ToggleStateHolder
import com.chronoio.chrono.presentation.objects.LockedStateProvider
import com.chronoio.chrono.presentation.relations.providers.DefaultObjectRelationProvider
import com.chronoio.chrono.presentation.relations.providers.DefaultObjectValueProvider
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationListProvider
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationProvider
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationProvider.Companion.INTRINSIC_PROVIDER_TYPE
import com.chronoio.chrono.presentation.relations.providers.ObjectValueProvider
import com.chronoio.chrono.presentation.templates.ObjectTypeTemplatesContainer
import com.chronoio.chrono.presentation.util.CopyFileToCacheDirectory
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.util.UrlHelper
import com.chronoio.chrono.presentation.util.defaultCopyFileToCacheDirectory
import com.chronoio.chrono.presentation.util.downloader.DebugTreeShareDownloader
import com.chronoio.chrono.presentation.util.downloader.DocumentFileShareDownloader
import com.chronoio.chrono.presentation.util.downloader.UriFileProvider
import com.chronoio.chrono.presentation.widgets.collection.ResourceProvider
import com.chronoio.chrono.providers.DefaultCoverImageHashProvider
import com.chronoio.chrono.ui.editor.EditorFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named
import kotlinx.coroutines.Dispatchers

@Subcomponent(
    modules = [
        EditorSessionModule::class,
        EditorUseCaseModule::class,
    ]
)
@PerScreen
interface EditorSubComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withParams(params: EditorViewModel.Params): Builder
        fun session(module: EditorSessionModule): Builder
        fun usecase(module: EditorUseCaseModule): Builder
        fun build(): EditorSubComponent
    }

    fun inject(fragment: EditorFragment)

    fun objectIconPickerComponent(): ObjectIconPickerComponent.Builder
    fun textBlockIconPickerComponent(): TextBlockIconPickerComponent.Builder

    // Relations

    fun objectRelationListComponent(): ObjectRelationListComponent.Builder
    fun relationAddToObjectComponent(): RelationAddToObjectSubComponent.Builder
    fun relationCreateFromScratchForObjectComponent(): RelationCreateFromScratchForObjectSubComponent.Builder
    fun relationCreateFromScratchForObjectBlockComponent(): RelationCreateFromScratchForObjectBlockSubComponent.Builder
    fun relationTextValueComponent(): RelationTextValueSubComponent.Builder
    fun editRelationDateComponent(): DefaultRelationDataValueSubComponent.Builder

    fun objectCoverComponent(): SelectCoverObjectSubComponent.Builder
    fun objectUnsplashComponent(): UnsplashSubComponent.Builder
    fun objectMenuComponent(): ObjectMenuComponent.Builder

    fun objectAppearanceSettingComponent(): ObjectAppearanceSettingSubComponent.Builder
    fun objectAppearanceIconComponent(): ObjectAppearanceIconSubComponent.Builder
    fun objectAppearancePreviewLayoutComponent(): ObjectAppearancePreviewLayoutSubComponent.Builder
    fun objectAppearanceChooseDescription(): ObjectAppearanceChooseDescriptionSubComponent.Builder

    fun setBlockTextValueComponent(): SetBlockTextValueSubComponent.Builder

    fun linkToObjectOrWebBuilder(): LinkToObjectOrWebSubComponent.Builder

    fun tagStatusObjectComponent(): TagOrStatusValueObjectComponent.Builder
    fun optionObjectComponent(): CreateOrEditOptionObjectComponent.Builder
    fun objectValueComponent(): ObjectValueObjectComponent.Builder
    fun versionHistoryComponent() : VersionHistoryComponent.Builder
}


/**
 * Session-related dependencies, session being defined as active work with a document visible to our user.
 * Hence, these dependencies are stateful and therefore should not be shared between different sessions of the same document.
 * Consider the following navigation scenario: Document A > Document B > Document A'.
 * In this case, stateful dependencies should not be shared between A and A'.
 */
@Module
object EditorSessionModule {

    @JvmStatic
    @Provides
    fun provideToggler(): ToggleStateHolder = ToggleStateHolder.Default()

    @JvmStatic
    @Provides
    fun provideProxer(): Editor.Proxer = Editor.Proxer()

    @JvmStatic
    @Provides
    @PerScreen
    fun provideStorage(): Editor.Storage = Editor.Storage()

    @JvmStatic
    @Provides
    @PerScreen
    fun lockedStateProvider(
        storage: Editor.Storage
    ) : LockedStateProvider = LockedStateProvider.EditorLockedStateProvider(
        storage = storage
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun relationListProvider(
        storage: Editor.Storage
    ) : ObjectRelationListProvider = ObjectRelationListProvider.EditorRelationListProvider(
        storage = storage
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideFileLimitEvents(
        channel: FileLimitsEventChannel,
        dispatchers: AppCoroutineDispatchers
    ) : InterceptFileLimitEvents = InterceptFileLimitEvents(channel, dispatchers)

    @JvmStatic
    @Provides
    fun providePageViewModelFactory(
        params: EditorViewModel.Params,
        permissions: UserPermissionProvider,
        openPage: OpenPage,
        closePage: CloseObject,
        interceptEvents: InterceptEvents,
        updateLinkMarks: UpdateLinkMarks,
        removeLinkMark: RemoveLinkMark,
        createObjectSet: CreateObjectSet,
        createBlockLinkWithObject: CreateBlockLinkWithObject,
        createObjectAsMentionOrLink: CreateObjectAsMentionOrLink,
        documentExternalEventReducer: DocumentExternalEventReducer,
        urlBuilder: UrlBuilder,
        renderer: DefaultBlockViewRenderer,
        orchestrator: Orchestrator,
        analytics: Analytics,
        dispatcher: Dispatcher<Payload>,
        delegator: Delegator<Action>,
        updateDetail: UpdateDetail,
        searchObjects: SearchObjects,
        getDefaultObjectType: GetDefaultObjectType,
        findObjectSetForType: FindObjectSetForType,
        copyFileToCacheDirectory: CopyFileToCacheDirectory,
        downloadUnsplashImage: DownloadUnsplashImage,
        setDocCoverImage: SetDocCoverImage,
        setDocImageIcon: SetDocumentImageIcon,
        createObject: CreateObject,
        storeOfRelations: StoreOfRelations,
        storeOfObjectTypes: StoreOfObjectTypes,
        objectToSet: ConvertObjectToSet,
        featureToggles: FeatureToggles,
        tableDelegate: EditorTableDelegate,
        spaceManager: SpaceManager,
        getObjectTypes: GetObjectTypes,
        objectToCollection: ConvertObjectToCollection,
        interceptFileLimitEvents: InterceptFileLimitEvents,
        addRelationToObject: AddRelationToObject,
        applyTemplate: ApplyTemplate,
        setObjectType: SetObjectType,
        templatesContainer: ObjectTypeTemplatesContainer,
        storelessSubscriptionContainer: StorelessSubscriptionContainer,
        dispatchers: AppCoroutineDispatchers,
        analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        syncStatusProvider: SpaceSyncAndP2PStatusProvider,
        getNetworkMode: GetNetworkMode,
        clearLastOpenedObject: ClearLastOpenedObject,
        fieldParser: FieldParser,
        dateProvider: DateProvider,
        spaceViews: SpaceViewSubscriptionContainer,
        urlHelper: UrlHelper,
        addDiscussion: AddDiscussion,
        getChatMessages: GetChatMessages,
        backHistoryDelegate: BackHistoryDelegate,
        exitToVaultDelegate: ExitToVaultDelegate,
        typeSuggestionEngine: TypeSuggestionEngine
    ): EditorViewModelFactory = EditorViewModelFactory(
        params = params,
        permissions = permissions,
        openPage = openPage,
        closeObject = closePage,
        createBlockLinkWithObject = createBlockLinkWithObject,
        createObjectAsMentionOrLink = createObjectAsMentionOrLink,
        interceptEvents = interceptEvents,
        updateLinkMarks = updateLinkMarks,
        removeLinkMark = removeLinkMark,
        documentEventReducer = documentExternalEventReducer,
        urlBuilder = urlBuilder,
        renderer = renderer,
        orchestrator = orchestrator,
        analytics = analytics,
        dispatcher = dispatcher,
        delegator = delegator,
        updateDetail = updateDetail,
        searchObjects = searchObjects,
        getDefaultObjectType = getDefaultObjectType,
        findObjectSetForType = findObjectSetForType,
        createObjectSet = createObjectSet,
        copyFileToCacheDirectory = copyFileToCacheDirectory,
        downloadUnsplashImage = downloadUnsplashImage,
        setDocCoverImage = setDocCoverImage,
        setDocImageIcon = setDocImageIcon,
        createObject = createObject,
        storeOfRelations = storeOfRelations,
        storeOfObjectTypes = storeOfObjectTypes,
        objectToSet = objectToSet,
        featureToggles = featureToggles,
        tableDelegate = tableDelegate,
        spaceManager = spaceManager,
        getObjectTypes = getObjectTypes,
        objectToCollection = objectToCollection,
        interceptFileLimitEvents = interceptFileLimitEvents,
        addRelationToObject = addRelationToObject,
        applyTemplate = applyTemplate,
        setObjectType = setObjectType,
        templatesContainer = templatesContainer,
        dispatchers = dispatchers,
        storelessSubscriptionContainer = storelessSubscriptionContainer,
        getNetworkMode = getNetworkMode,
        analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
        clearLastOpenedObject = clearLastOpenedObject,
        syncStatusProvider = syncStatusProvider,
        fieldParser = fieldParser,
        dateProvider = dateProvider,
        spaceViews = spaceViews,
        urlHelper = urlHelper,
        addDiscussion = addDiscussion,
        getChatMessages = getChatMessages,
        backHistoryDelegate = backHistoryDelegate,
        exitToVaultDelegate = exitToVaultDelegate,
        typeSuggestionEngine = typeSuggestionEngine
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
    fun provideTableDelegate(
        dispatcher: Dispatcher<Payload>,
        createTableColumn: CreateTableColumn,
        createTableRow: CreateTableRow,
        deleteTableColumn: DeleteTableColumn,
        deleteTableRow: DeleteTableRow,
        duplicateTableRow: DuplicateTableRow,
        duplicateTableColumn: DuplicateTableColumn,
        fillTableRow: FillTableRow,
        fillTableColumn: FillTableColumn,
        moveTableRow: MoveTableRow,
        moveTableColumn: MoveTableColumn,
        setTableRowHeader: SetTableRowHeader
    ): EditorTableDelegate = DefaultEditorTableDelegate(
        dispatcher = dispatcher,
        createTableColumn = createTableColumn,
        createTableRow = createTableRow,
        deleteTableColumn = deleteTableColumn,
        deleteTableRow = deleteTableRow,
        duplicateTableColumn = duplicateTableColumn,
        duplicateTableRow = duplicateTableRow,
        fillTableColumn = fillTableColumn,
        fillTableRow = fillTableRow,
        moveTableColumn = moveTableColumn,
        moveTableRow = moveTableRow,
        setTableRowHeader = setTableRowHeader
    )

    @JvmStatic
    @Provides
    fun provideDefaultBlockViewRenderer(
        urlBuilder: UrlBuilder,
        toggleStateHolder: ToggleStateHolder,
        coverImageHashProvider: CoverImageHashProvider,
        storeOfRelations: StoreOfRelations,
        storeOfObjectTypes: StoreOfObjectTypes,
        fieldParser: FieldParser,
        resourceProvider: ResourceProvider
    ): DefaultBlockViewRenderer = DefaultBlockViewRenderer(
        urlBuilder = urlBuilder,
        toggleStateHolder = toggleStateHolder,
        coverImageHashProvider = coverImageHashProvider,
        storeOfRelations = storeOfRelations,
        storeOfObjectTypes = storeOfObjectTypes,
        fieldParser = fieldParser,
        resourceProvider = resourceProvider
    )

    @JvmStatic
    @Provides
    fun provideCoverImageHashProvider(): CoverImageHashProvider = DefaultCoverImageHashProvider()

    @JvmStatic
    @Provides
    fun provideDocumentExternalEventReducer(): DocumentExternalEventReducer =
        DocumentExternalEventReducer()

    @JvmStatic
    @Provides
    fun providePatternMatcher(): DefaultPatternMatcher = DefaultPatternMatcher()

    @JvmStatic
    @Provides
    fun provideSelectionStateHolder(): SelectionStateHolder = SelectionStateHolder.Default()

    @JvmStatic
    @Provides
    fun provideMemory(
        selectionStateHolder: SelectionStateHolder
    ): Editor.Memory = Editor.Memory(
        selections = selectionStateHolder
    )

    @JvmStatic
    @Provides
    fun provideInteractor(
        storage: Editor.Storage,
        proxer: Editor.Proxer,
        memory: Editor.Memory,
        createBlock: CreateBlock,
        replaceBlock: ReplaceBlock,
        duplicateBlock: DuplicateBlock,
        updateTextColor: UpdateTextColor,
        updateBackgroundColor: UpdateBackgroundColor,
        splitBlock: SplitBlock,
        mergeBlocks: MergeBlocks,
        unlinkBlocks: UnlinkBlocks,
        updateDivider: UpdateDivider,
        updateTextStyle: UpdateTextStyle,
        turnInto: TurnIntoStyle,
        updateCheckbox: UpdateCheckbox,
        downloadFile: DownloadFile,
        updateText: UpdateText,
        uploadBlock: UploadBlock,
        updateFields: UpdateFields,
        updateAlignment: UpdateAlignment,
        setupBookmark: SetupBookmark,
        createBookmarkBlock: CreateBookmarkBlock,
        turnIntoDocument: TurnIntoDocument,
        createTable: CreateTable,
        fillTableRow: FillTableRow,
        matcher: DefaultPatternMatcher,
        move: Move,
        copy: Copy,
        paste: Paste,
        undo: Undo,
        redo: Redo,
        setRelationKey: SetRelationKey,
        analytics: Analytics,
        updateBlocksMark: UpdateBlocksMark,
        documentFileShareDownloader: DocumentFileShareDownloader,
        clearBlockContent: ClearBlockContent,
        clearBlockStyle: ClearBlockStyle,
        analyticsSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        spaceManager: SpaceManager
    ): Orchestrator = Orchestrator(
        stores = storage,
        createBlock = createBlock,
        replaceBlock = replaceBlock,
        proxies = proxer,
        duplicateBlock = duplicateBlock,
        updateBackgroundColor = updateBackgroundColor,
        updateTextColor = updateTextColor,
        uploadBlock = uploadBlock,
        splitBlock = splitBlock,
        mergeBlocks = mergeBlocks,
        unlinkBlocks = unlinkBlocks,
        undo = undo,
        redo = redo,
        updateTextStyle = updateTextStyle,
        updateCheckbox = updateCheckbox,
        updateDivider = updateDivider,
        memory = memory,
        downloadFile = downloadFile,
        documentFileShareDownloader = documentFileShareDownloader,
        turnIntoDocument = turnIntoDocument,
        textInteractor = Interactor.TextInteractor(
            proxies = proxer,
            stores = storage,
            matcher = matcher
        ),
        updateText = updateText,
        updateAlignment = updateAlignment,
        setupBookmark = setupBookmark,
        createBookmarkBlock = createBookmarkBlock,
        move = move,
        paste = paste,
        copy = copy,
        setRelationKey = setRelationKey,
        analytics = analytics,
        updateFields = updateFields,
        turnIntoStyle = turnInto,
        updateBlocksMark = updateBlocksMark,
        createTable = createTable,
        fillTableRow = fillTableRow,
        clearBlockContent = clearBlockContent,
        clearBlockStyle = clearBlockStyle,
        analyticsSpaceHelperDelegate = analyticsSpaceHelperDelegate,
        spaceManager = spaceManager
    )
}

/**
 * These dependencies are stateless and therefore should be shared between different sessions of the same document.
 * In other words, if document A is opened twice (as A and A'), use-cases will be shared between all its sessions.
 * Consider the following navigation scenario: Document A > Document B > Document A'
 * The same documents have the same use-case dependencies, in order to avoid instance-creation overhead.
 */
@Module
object EditorUseCaseModule {

    @JvmStatic
    @Provides
    @PerScreen
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
    @PerScreen
    fun provideClosePageUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CloseObject = CloseObject(
        repo = repo,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateBlockUseCase(
        repo: BlockRepository
    ): UpdateText = UpdateText(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateBlockUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreateBlock = CreateBlock(
        repo = repo,
        dispatchers = dispatchers
    )

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
    fun provideUpdateCheckboxUseCase(
        repo: BlockRepository
    ): UpdateCheckbox = UpdateCheckbox(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUnlinkBlocksUseCase(
        repo: BlockRepository
    ): UnlinkBlocks = UnlinkBlocks(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDuplicateBlockUseCase(
        repo: BlockRepository
    ): DuplicateBlock = DuplicateBlock(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateDivider(
        repo: BlockRepository
    ): UpdateDivider = UpdateDivider(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideMergeBlocksUseCase(
        repo: BlockRepository
    ): MergeBlocks = MergeBlocks(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSplitBlockUseCase(
        repo: BlockRepository
    ): SplitBlock = SplitBlock(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetRelationKeyUseCase(
        repo: BlockRepository
    ): SetRelationKey = SetRelationKey(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUploadUrl(
        repo: BlockRepository
    ): UploadBlock = UploadBlock(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateBlocksMark(
        repo: BlockRepository
    ): UpdateBlocksMark = UpdateBlocksMark(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateLinkMarks(): UpdateLinkMarks = UpdateLinkMarks()

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRemoveLinkMark(): RemoveLinkMark = RemoveLinkMark()

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateTextStyleUseCase(
        repo: BlockRepository
    ): UpdateTextStyle = UpdateTextStyle(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateTextColorUseCase(
        repo: BlockRepository
    ): UpdateTextColor = UpdateTextColor(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateBackgroundColorUseCase(
        repo: BlockRepository
    ): UpdateBackgroundColor = UpdateBackgroundColor(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDownloadFileUseCase(
        downloader: Downloader
    ): DownloadFile = DownloadFile(
        downloader = downloader,
        context = Dispatchers.Main
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateObjectUseCase(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): CreateBlockLinkWithObject =
        CreateBlockLinkWithObject(
            repo = repo,
            dispatchers = dispatchers
        )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateObjectAsMentionOrLink(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers,
        spaceManager: SpaceManager
    ): CreateObjectAsMentionOrLink = CreateObjectAsMentionOrLink(
        repo = repo,
        dispatchers = dispatchers,
        spaceManager = spaceManager
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDuplicateObject(
        repo: BlockRepository
    ): DuplicateObject = DuplicateObject(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideReplaceBlockUseCase(
        repo: BlockRepository
    ): ReplaceBlock = ReplaceBlock(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateTitleUseCase(
        repo: BlockRepository
    ): UpdateTitle = UpdateTitle(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateAlignmentUseCase(
        repo: BlockRepository
    ): UpdateAlignment = UpdateAlignment(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetupBookmarkUseCase(
        repo: BlockRepository
    ): SetupBookmark = SetupBookmark(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateBookmarkUseCase(
        repo: BlockRepository
    ): CreateBookmarkBlock = CreateBookmarkBlock(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideClipboardPasteUseCase(
        repo: BlockRepository,
        clipboard: Clipboard,
        matcher: Clipboard.UriMatcher
    ): Paste = Paste(
        repo = repo,
        clipboard = clipboard,
        matcher = matcher
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCopyUseCase(
        repo: BlockRepository,
        clipboard: Clipboard
    ): Copy = Copy(
        repo = repo,
        clipboard = clipboard
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideTurnIntoDocumentUseCase(
        repo: BlockRepository
    ): TurnIntoDocument = TurnIntoDocument(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateFieldsUseCase(
        repo: BlockRepository
    ): UpdateFields = UpdateFields(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetLinkAppearance(
        repo: BlockRepository
    ): SetLinkAppearance = SetLinkAppearance(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateTableUseCase(
        repo: BlockRepository
    ): CreateTable = CreateTable(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideTableRowFill(
        repo: BlockRepository
    ): FillTableRow = FillTableRow(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideObjectToSet(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): ConvertObjectToSet = ConvertObjectToSet(
        repo = repo,
        dispatchers = dispatchers
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
    @Named(INTRINSIC_PROVIDER_TYPE)
    fun provideDefaultObjectRelationProvider(
        storage: Editor.Storage,
        storeOfRelations: StoreOfRelations
    ): ObjectRelationProvider = DefaultObjectRelationProvider(
        storage = storage,
        storeOfRelations = storeOfRelations
    )

    @JvmStatic
    @Provides
    @PerScreen
    @Named(INTRINSIC_PROVIDER_TYPE)
    fun provideDefaultObjectValueProvider(
        storage: Editor.Storage
    ): ObjectValueProvider = DefaultObjectValueProvider(storage.details)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDelegator(): Delegator<Action> = Delegator.Default()

    @JvmStatic
    @Provides
    @PerScreen
    fun provideTurnIntoUseCase(repo: BlockRepository): TurnIntoStyle = TurnIntoStyle(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideUpdateDetailUseCase(
        repository: BlockRepository
    ): UpdateDetail = UpdateDetail(repository)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideAddFileToObjectUseCase(
        repo: BlockRepository
    ): AddFileToObject = AddFileToObject(repo = repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetObjectType(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectType = SetObjectType(repo, dispatchers)

    @JvmStatic
    @PerScreen
    @Provides
    fun provideGetDefaultPageType(
        repo: UserSettingsRepository,
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetDefaultObjectType = GetDefaultObjectType(
        userSettingsRepository = repo,
        blockRepository = blockRepository,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun findObjectSetForType(
        repo: BlockRepository
    ): FindObjectSetForType = FindObjectSetForType(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCreateObjectSetUseCase(
        repo: BlockRepository
    ): CreateObjectSet = CreateObjectSet(repo = repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideCopyFileToCache(
        context: Context
    ): CopyFileToCacheDirectory = defaultCopyFileToCacheDirectory(context)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideDownload(repo: UnsplashRepository): DownloadUnsplashImage = DownloadUnsplashImage(
        repo = repo
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDocCoverImageUseCase(
        repo: BlockRepository
    ): SetDocCoverImage = SetDocCoverImage(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetDocumentImageIconUseCase(
        repo: BlockRepository
    ): SetDocumentImageIcon = SetDocumentImageIcon(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun applyTemplates(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): ApplyTemplate =
        ApplyTemplate(
            repo = repo,
            dispatchers = dispatchers
        )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideAddRelationToObject(repo: BlockRepository, dispatchers: AppCoroutineDispatchers) =
        AddRelationToObject(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun providesDocumentFileShareDownloader(
        repo: BlockRepository,
        context: Context,
        fileProvider: UriFileProvider,
        dispatchers: AppCoroutineDispatchers
    ): DocumentFileShareDownloader = DocumentFileShareDownloader(
        repo = repo,
        context = context.applicationContext,
        uriFileProvider = fileProvider,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun providesDebugTreeShareDownloader(
        repo: BlockRepository,
        context: Context,
        fileProvider: UriFileProvider,
        dispatchers: AppCoroutineDispatchers
    ): DebugTreeShareDownloader = DebugTreeShareDownloader(
        repo = repo,
        context = context.applicationContext,
        uriFileProvider = fileProvider,
        dispatchers = dispatchers
    )

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockListClearContent(
        repo: BlockRepository
    ): ClearBlockContent = ClearBlockContent(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockListClearStyle(
        repo: BlockRepository
    ): ClearBlockStyle = ClearBlockStyle(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableCreateColumn(
        repo: BlockRepository
    ): CreateTableColumn = CreateTableColumn(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableCreateRow(
        repo: BlockRepository
    ): CreateTableRow = CreateTableRow(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableDeleteRow(
        repo: BlockRepository
    ): DeleteTableRow = DeleteTableRow(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableDeleteColumn(
        repo: BlockRepository
    ): DeleteTableColumn = DeleteTableColumn(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableDuplicateColumn(
        repo: BlockRepository
    ): DuplicateTableColumn = DuplicateTableColumn(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableDuplicateRow(
        repo: BlockRepository
    ): DuplicateTableRow = DuplicateTableRow(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableFillColumn(
        repo: BlockRepository
    ): FillTableColumn = FillTableColumn(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableMoveColumn(
        repo: BlockRepository
    ): MoveTableColumn = MoveTableColumn(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableMoveRow(
        repo: BlockRepository
    ): MoveTableRow = MoveTableRow(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideBlockTableSetHeaderRow(
        repo: BlockRepository
    ): SetTableRowHeader = SetTableRowHeader(repo)

    @JvmStatic
    @Provides
    @PerScreen
    fun getOptions(repo: BlockRepository) = GetOptions(repo)

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
}