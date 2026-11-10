package com.chronoio.chrono.presentation.editor.editor

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.ObjectViewDetails
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Event
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.NetworkModeConfig
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.Response
import com.chronoio.chrono.core_models.Struct
import com.chronoio.chrono.core_models.StubObjectType
import com.chronoio.chrono.core_models.StubSpaceView
import com.chronoio.chrono.core_models.multiplayer.SpaceMemberPermissions
import com.chronoio.chrono.core_models.multiplayer.SpaceUxType
import com.chronoio.chrono.core_models.primitives.ParsedProperties
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.primitives.TypeId
import com.chronoio.chrono.core_models.primitives.TypeKey
import com.chronoio.chrono.core_models.restrictions.ObjectRestriction
import com.chronoio.chrono.domain.ai.TypeSuggestionEngine
import com.chronoio.chrono.domain.auth.interactor.ClearLastOpenedObject
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.base.Result
import com.chronoio.chrono.domain.base.Resultat
import com.chronoio.chrono.domain.block.UpdateDivider
import com.chronoio.chrono.domain.block.interactor.ClearBlockContent
import com.chronoio.chrono.domain.block.interactor.ClearBlockStyle
import com.chronoio.chrono.domain.block.interactor.CreateBlock
import com.chronoio.chrono.domain.block.interactor.DuplicateBlock
import com.chronoio.chrono.domain.block.interactor.MergeBlocks
import com.chronoio.chrono.domain.block.interactor.Move
import com.chronoio.chrono.domain.block.interactor.RemoveLinkMark
import com.chronoio.chrono.domain.block.interactor.ReplaceBlock
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
import com.chronoio.chrono.domain.clipboard.Copy
import com.chronoio.chrono.domain.clipboard.Paste
import com.chronoio.chrono.domain.config.Gateway
import com.chronoio.chrono.domain.cover.SetDocCoverImage
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.download.DownloadFile
import com.chronoio.chrono.domain.event.interactor.InterceptEvents
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.icon.SetDocumentImageIcon
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.misc.UrlBuilderImpl
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.networkmode.GetNetworkMode
import com.chronoio.chrono.domain.`object`.ConvertObjectToCollection
import com.chronoio.chrono.domain.`object`.ConvertObjectToSet
import com.chronoio.chrono.domain.`object`.SetObjectInternalFlags
import com.chronoio.chrono.domain.`object`.UpdateDetail
import com.chronoio.chrono.domain.objects.DefaultStoreOfObjectTypes
import com.chronoio.chrono.domain.objects.DefaultStoreOfRelations
import com.chronoio.chrono.domain.objects.GetDateObjectByTimestamp
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.page.CloseObject
import com.chronoio.chrono.domain.page.CreateBlockLinkWithObject
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.page.CreateObjectAsMentionOrLink
import com.chronoio.chrono.domain.page.OpenPage
import com.chronoio.chrono.domain.page.Redo
import com.chronoio.chrono.domain.page.Undo
import com.chronoio.chrono.domain.page.bookmark.CreateBookmarkBlock
import com.chronoio.chrono.domain.page.bookmark.SetupBookmark
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.primitives.FieldParserImpl
import com.chronoio.chrono.domain.relations.AddRelationToObject
import com.chronoio.chrono.domain.relations.SetRelationKey
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.SearchObjects
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
import com.chronoio.chrono.domain.templates.GetTemplates
import com.chronoio.chrono.domain.unsplash.DownloadUnsplashImage
import com.chronoio.chrono.domain.unsplash.UnsplashRepository
import com.chronoio.chrono.domain.workspace.InterceptFileLimitEvents
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.common.Action
import com.chronoio.chrono.presentation.common.Delegator
import com.chronoio.chrono.presentation.editor.DocumentExternalEventReducer
import com.chronoio.chrono.presentation.editor.Editor
import com.chronoio.chrono.presentation.editor.EditorViewModel
import com.chronoio.chrono.presentation.editor.cover.CoverImageHashProvider
import com.chronoio.chrono.presentation.editor.editor.pattern.DefaultPatternMatcher
import com.chronoio.chrono.presentation.editor.editor.table.DefaultEditorTableDelegate
import com.chronoio.chrono.presentation.editor.editor.table.EditorTableDelegate
import com.chronoio.chrono.presentation.editor.render.DefaultBlockViewRenderer
import com.chronoio.chrono.presentation.editor.selection.SelectionStateHolder
import com.chronoio.chrono.presentation.editor.toggle.ToggleStateHolder
import com.chronoio.chrono.presentation.home.UserPermissionProviderStub
import com.chronoio.chrono.presentation.templates.ObjectTypeTemplatesContainer
import com.chronoio.chrono.presentation.util.CopyFileToCacheDirectory
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.util.dispatchers
import com.chronoio.chrono.presentation.util.downloader.DocumentFileShareDownloader
import com.chronoio.chrono.presentation.widgets.collection.ResourceProvider
import com.chronoio.chrono.test_utils.MockDataFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

open class EditorPresentationTestSetup {

    val root: Id = MockDataFactory.randomString()
    val defaultSpace: Id = MockDataFactory.randomUuid()

    @Mock
    lateinit var openPage: OpenPage

    @Mock
    lateinit var closePage: CloseObject

    @Mock
    lateinit var interceptEvents: InterceptEvents

    @Mock
    lateinit var createBlock: CreateBlock

    @Mock
    lateinit var updateText: UpdateText

    @Mock
    lateinit var updateCheckbox: UpdateCheckbox

    @Mock
    lateinit var duplicateBlock: DuplicateBlock

    @Mock
    lateinit var unlinkBlocks: UnlinkBlocks

    @Mock
    lateinit var updateTextStyle: UpdateTextStyle

    @Mock
    lateinit var updateTextColor: UpdateTextColor

    @Mock
    lateinit var clearLastOpenedObject: ClearLastOpenedObject

    @Mock
    lateinit var updateBlocksMark: UpdateBlocksMark

    @Mock
    lateinit var updateDivider: UpdateDivider

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var updateLinkMark: UpdateLinkMarks

    @Mock
    lateinit var removeLinkMark: RemoveLinkMark

    @Mock
    lateinit var mergeBlocks: MergeBlocks

    @Mock
    lateinit var splitBlock: SplitBlock

    @Mock
    lateinit var createBlockLinkWithObject: CreateBlockLinkWithObject

    @Mock
    lateinit var updateAlignment: UpdateAlignment

    @Mock
    lateinit var updateBackgroundColor: UpdateBackgroundColor

    @Mock
    lateinit var downloadFile: DownloadFile

    @Mock
    lateinit var documentFileShareDownloader: DocumentFileShareDownloader

    @Mock
    lateinit var uploadBlock: UploadBlock

    @Mock
    lateinit var updateFields: UpdateFields

    @Mock
    lateinit var paste: Paste

    @Mock
    lateinit var copy: Copy

    @Mock
    lateinit var getDateObjectByTimestamp: GetDateObjectByTimestamp

    @Mock
    lateinit var undo: Undo

    @Mock
    lateinit var redo: Redo

    @Mock
    lateinit var setupBookmark: SetupBookmark

    @Mock
    lateinit var resourceProvider: ResourceProvider

    @Mock
    lateinit var createBookmarkBlock: CreateBookmarkBlock

    @Mock
    lateinit var setRelationKey: SetRelationKey

    @Mock
    lateinit var createObjectAsMentionOrLink: CreateObjectAsMentionOrLink

    @Mock
    lateinit var replaceBlock: ReplaceBlock

    @Mock
    lateinit var move: Move

    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var turnIntoDocument: TurnIntoDocument

    @Mock
    lateinit var turnIntoStyle: TurnIntoStyle

    @Mock
    lateinit var gateway: Gateway

    @Mock
    lateinit var repo: BlockRepository

    @Mock
    lateinit var unsplashRepo: UnsplashRepository

    @Mock
    lateinit var coverImageHashProvider: CoverImageHashProvider

    @Mock
    lateinit var setObjectType: SetObjectType

    @Mock
    lateinit var searchObjects: SearchObjects

    @Mock
    lateinit var applyTemplate: ApplyTemplate

    @Mock
    lateinit var getDefaultObjectType: GetDefaultObjectType

    @Mock
    lateinit var findObjectSetForType: FindObjectSetForType

    @Mock
    lateinit var createObjectSet: CreateObjectSet

    @Mock
    lateinit var copyFileToCacheDirectory: CopyFileToCacheDirectory

    @Mock
    lateinit var createObject: CreateObject

    @Mock
    lateinit var getTemplates: GetTemplates

    @Mock
    lateinit var createTable: CreateTable

    @Mock
    lateinit var fillTableRow: FillTableRow

    @Mock
    lateinit var spacedViews: SpaceViewSubscriptionContainer

    @Mock
    lateinit var convertObjectToCollection: ConvertObjectToCollection

    lateinit var tableDelegate: EditorTableDelegate

    lateinit var dispatcher: Dispatcher<Payload>

    @Mock
    lateinit var templatesContainer: ObjectTypeTemplatesContainer

    protected val builder: UrlBuilder get() = UrlBuilderImpl(gateway)

    private lateinit var updateDetail: UpdateDetail
    private lateinit var downloadUnsplashImage: DownloadUnsplashImage
    private lateinit var setDocCoverImage: SetDocCoverImage
    private lateinit var setDocImageIcon: SetDocumentImageIcon
    private lateinit var objectToSet: ConvertObjectToSet
    private lateinit var clearBlockContent: ClearBlockContent
    private lateinit var clearBlockStyle: ClearBlockStyle

    @Mock
    lateinit var createTableColumn: CreateTableColumn

    @Mock
    lateinit var createTableRow: CreateTableRow

    @Mock
    lateinit var deleteTableColumn: DeleteTableColumn

    @Mock
    lateinit var deleteTableRow: DeleteTableRow

    @Mock
    lateinit var duplicateTableRow: DuplicateTableRow

    @Mock
    lateinit var duplicateTableColumn: DuplicateTableColumn

    @Mock
    lateinit var fillTableColumn: FillTableColumn

    @Mock
    lateinit var moveTableRow: MoveTableRow

    @Mock
    lateinit var moveTableColumn: MoveTableColumn

    @Mock
    lateinit var addRelationToObject: AddRelationToObject

    @Mock
    lateinit var setTableRowHeader: SetTableRowHeader

    open lateinit var orchestrator: Orchestrator

    private val delegator = Delegator.Default<Action>()

    protected val storeOfRelations: StoreOfRelations = DefaultStoreOfRelations()
    protected val storeOfObjectTypes: StoreOfObjectTypes = DefaultStoreOfObjectTypes()

    @Mock
    lateinit var getObjectTypes: GetObjectTypes

    @Mock
    lateinit var interceptFileLimitEvents: InterceptFileLimitEvents

    @Mock
    lateinit var setObjectInternalFlags: SetObjectInternalFlags

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var storelessSubscriptionContainer: StorelessSubscriptionContainer

    @Mock
    lateinit var analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate

    @Mock
    lateinit var getNetworkMode: GetNetworkMode

    @Mock
    lateinit var spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider

    @Mock
    lateinit var dateProvider: DateProvider

    @Mock
    lateinit var logger: Logger

    var permissions: UserPermissionProvider = UserPermissionProviderStub()

    val objTypeUniqueKey = "objTypeUniqueKey-${MockDataFactory.randomString()}"

    val objType = StubObjectType(
        id = MockDataFactory.randomUuid(),
        uniqueKey = objTypeUniqueKey,
    )

    open fun buildViewModel(urlBuilder: UrlBuilder = builder): EditorViewModel {

        runBlocking {
            storeOfObjectTypes.merge(
                types = listOf(objType)
            )
        }

        val storage = Editor.Storage()
        val proxies = Editor.Proxer()
        val memory = Editor.Memory(
            selections = SelectionStateHolder.Default()
        )
        objectToSet = ConvertObjectToSet(repo, dispatchers)
        updateDetail = UpdateDetail(repo)
        setDocCoverImage = SetDocCoverImage(repo)
        setDocImageIcon = SetDocumentImageIcon(repo)
        downloadUnsplashImage = DownloadUnsplashImage(unsplashRepo)
        clearBlockContent = ClearBlockContent(repo)
        clearBlockStyle = ClearBlockStyle(repo)

        orchestrator = Orchestrator(
            createBlock = createBlock,
            replaceBlock = replaceBlock,
            updateTextColor = updateTextColor,
            duplicateBlock = duplicateBlock,
            downloadFile = downloadFile,
            documentFileShareDownloader = documentFileShareDownloader,
            undo = undo,
            redo = redo,
            updateText = updateText,
            updateCheckbox = updateCheckbox,
            updateTextStyle = updateTextStyle,
            updateBackgroundColor = updateBackgroundColor,
            mergeBlocks = mergeBlocks,
            uploadBlock = uploadBlock,
            splitBlock = splitBlock,
            unlinkBlocks = unlinkBlocks,
            updateDivider = updateDivider,
            memory = memory,
            stores = storage,
            proxies = proxies,
            textInteractor = Interactor.TextInteractor(
                proxies = proxies,
                stores = storage,
                matcher = DefaultPatternMatcher()
            ),
            updateAlignment = updateAlignment,
            setupBookmark = setupBookmark,
            createBookmarkBlock = createBookmarkBlock,
            paste = paste,
            copy = copy,
            move = move,
            turnIntoDocument = turnIntoDocument,
            analytics = analytics,
            updateFields = updateFields,
            setRelationKey = setRelationKey,
            turnIntoStyle = turnIntoStyle,
            updateBlocksMark = updateBlocksMark,
            createTable = createTable,
            fillTableRow = fillTableRow,
            clearBlockContent = clearBlockContent,
            clearBlockStyle = clearBlockStyle,
            analyticsSpaceHelperDelegate = analyticSpaceHelperDelegate,
            spaceManager = spaceManager
        )

        dispatcher = Dispatcher.Default()

        tableDelegate = DefaultEditorTableDelegate(
            dispatcher, createTableColumn, createTableRow,
            deleteTableColumn, deleteTableRow, duplicateTableRow,
            duplicateTableColumn, fillTableRow, fillTableColumn,
            moveTableRow, moveTableColumn, setTableRowHeader
        )

        return EditorViewModel(
            openPage = openPage,
            closePage = closePage,
            createBlockLinkWithObject = createBlockLinkWithObject,
            createObjectAsMentionOrLink = createObjectAsMentionOrLink,
            interceptEvents = interceptEvents,
            updateLinkMarks = updateLinkMark,
            removeLinkMark = removeLinkMark,
            reducer = DocumentExternalEventReducer(),
            urlBuilder = urlBuilder,
            renderer = DefaultBlockViewRenderer(
                urlBuilder = urlBuilder,
                toggleStateHolder = ToggleStateHolder.Default(),
                coverImageHashProvider = coverImageHashProvider,
                storeOfRelations = storeOfRelations,
                storeOfObjectTypes = storeOfObjectTypes,
                fieldParser = fieldParser,
                resourceProvider = resourceProvider
            ),
            orchestrator = orchestrator,
            analytics = analytics,
            dispatcher = dispatcher,
            delegator = delegator,
            updateDetail = updateDetail,
            searchObjects = searchObjects,
            getDefaultObjectType = getDefaultObjectType,
            findObjectSetForType = findObjectSetForType,
            createObjectSet = createObjectSet,
            copyFileToCache = copyFileToCacheDirectory,
            downloadUnsplashImage = downloadUnsplashImage,
            setDocCoverImage = setDocCoverImage,
            setDocImageIcon = setDocImageIcon,
            createObject = createObject,
            objectToSet = objectToSet,
            objectToCollection = convertObjectToCollection,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            featureToggles = mock(),
            tableDelegate = tableDelegate,
            getObjectTypes = getObjectTypes,
            interceptFileLimitEvents = interceptFileLimitEvents,
            addRelationToObject = addRelationToObject,
            spaceManager = spaceManager,
            applyTemplate = applyTemplate,
            setObjectType = setObjectType,
            templatesContainer = templatesContainer,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            dispatchers = dispatchers,
            vmParams = EditorViewModel.Params(
                ctx = root,
                space = SpaceId(defaultSpace)
            ),
            permissions = permissions,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            clearLastOpenedObject = clearLastOpenedObject,
            spaceSyncAndP2PStatusProvider = spaceSyncAndP2PStatusProvider,
            getNetworkMode = getNetworkMode,
            fieldParser = fieldParser,
            dateProvider = dateProvider,
            spaceViews = spacedViews,
            urlHelper = mock(),
            addDiscussion = mock(),
            getChatMessages = mock(),
            backHistoryDelegate = mock(),
            exitToVaultDelegate = mock(),
            typeSuggestionEngine = TypeSuggestionEngine.NoOp
        )
    }

    fun stubParsedProperties() {
        fieldParser.stub {
            onBlocking {
                getObjectParsedProperties(
                    objectType = any(),
                    objPropertiesKeys = any(),
                    storeOfRelations = any()
                )
            } doReturn ParsedProperties()
        }
    }

    fun stubGetNetworkMode() {
        getNetworkMode.stub {
            onBlocking { run(Unit) } doReturn NetworkModeConfig()
        }
    }

    fun stubOpenDocument(
        document: List<Block> = emptyList(),
        details: ObjectViewDetails = ObjectViewDetails(
            details = mapOf(
                root to mapOf(
                    Relations.ID to root,
                    Relations.TYPE to listOf(objType.id)
                )
            )
        ),
        objectRestrictions: List<ObjectRestriction> = emptyList(),
        spaceId: SpaceId = SpaceId(defaultSpace)
    ) {
        openPage.stub {
            onBlocking {
                async(
                    OpenPage.Params(
                        obj = root,
                        saveAsLastOpened = true,
                        space = spaceId
                    )
                )
            } doReturn Resultat.success(
                Result.Success(
                    Payload(
                        context = root,
                        events = listOf(
                            Event.Command.ShowObject(
                                context = root,
                                root = root,
                                details = details.details,
                                blocks = document,
                                objectRestrictions = objectRestrictions
                            )
                        )
                    )
                )
            )
        }
    }

    fun stubMove() {
        move.stub {
            onBlocking { async(any()) } doReturn Resultat.Success(
                Payload(
                    context = root,
                    events = emptyList()
                )
            )
        }
    }

    fun stubInterceptEvents(
        params: InterceptEvents.Params = InterceptEvents.Params(context = root),
        flow: Flow<List<Event>> = flowOf(),
        stubInterceptThreadStatus: Boolean = true
    ) {
        interceptEvents.stub {
            onBlocking { build(params) } doReturn flow
        }
        if (stubInterceptThreadStatus) stubInterceptThreadStatus()
    }

    fun stubInterceptThreadStatus() {
        spaceSyncAndP2PStatusProvider.stub {
            onBlocking { observe() } doReturn emptyFlow()
        }
    }

    fun stubUpdateTextStyle(
        params: UpdateTextStyle.Params? = null,
        events: List<Event> = emptyList()
    ) {
        updateTextStyle.stub {
            onBlocking { invoke(params ?: any()) } doReturn Either.Right(
                Payload(
                    context = root,
                    events = events
                )
            )
        }
    }

    fun stubTurnIntoStyle(
        params: TurnIntoStyle.Params? = null,
        events: List<Event> = emptyList()
    ) {
        turnIntoStyle.stub {
            onBlocking { invoke(params ?: any()) } doReturn Either.Right(
                Payload(
                    context = root,
                    events = events
                )
            )
        }
    }

    fun stubUnlinkBlocks(
        params: UnlinkBlocks.Params? = null,
        events: List<Event> = emptyList()
    ) {
        unlinkBlocks.stub {
            onBlocking {
                if (params == null) invoke(any()) else invoke(params)
            } doReturn Either.Right(
                Payload(
                    context = root,
                    events = events
                )
            )
        }
    }

    fun stubCreateBlock(root: String) {
        createBlock.stub {
            onBlocking { async(any()) } doReturn Resultat.success(
                Pair(
                    MockDataFactory.randomString(), Payload(
                        context = root,
                        events = listOf()
                    )
                )
            )

        }
    }

    fun stubMergeBlocks(root: String) {
        mergeBlocks.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Payload(
                    context = root,
                    events = emptyList()
                )
            )
        }
    }

    fun stubCreateBlockLinkWithObject(root: String, target: String) {
        createBlockLinkWithObject.stub {
            onBlocking {
                async(any())
            } doReturn Resultat.success(
                CreateBlockLinkWithObject.Result(
                    id = root,
                    objectId = target,
                    payload = Payload(
                        context = root,
                        events = emptyList()
                    ),
                    details = emptyMap()
                )
            )
        }
    }

    fun stubUpdateText() {
        updateText.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(Unit)
        }
    }

    fun stubCopy() {
        copy.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(Unit)
        }
    }

    fun stubPaste() {
        paste.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Response.Clipboard.Paste(
                    0,
                    true,
                    emptyList(),
                    Payload("", emptyList())
                )
            )
        }
    }


    fun stubReplaceBlock() {
        replaceBlock.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Pair(
                    MockDataFactory.randomUuid(),
                    Payload(context = root, events = emptyList())
                )
            )
        }
    }

    /**
     * Stubs [replaceBlock] with the full identity-fork swap payload: [forked]
     * is added, [empty] is deleted, and the root's children become [children].
     */
    fun stubReplaceBlockWithSwap(empty: Block, forked: Block, children: List<Id>) {
        replaceBlock.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Pair(
                    forked.id,
                    Payload(
                        context = root,
                        events = listOf(
                            Event.Command.UpdateStructure(
                                context = root,
                                id = root,
                                children = children
                            ),
                            Event.Command.AddBlock(
                                context = root,
                                blocks = listOf(forked)
                            ),
                            Event.Command.DeleteBlock(
                                context = root,
                                targets = listOf(empty.id)
                            )
                        )
                    )
                )
            )
        }
    }

    /**
     * Stubs [createBlock] with the trailing-placeholder swap payload: [created]
     * is added and the root's children become [children].
     */
    fun stubCreateBlockWithSwap(created: Block, children: List<Id>) {
        createBlock.stub {
            onBlocking { async(any()) } doReturn Resultat.success(
                Pair(
                    created.id,
                    Payload(
                        context = root,
                        events = listOf(
                            Event.Command.AddBlock(
                                context = root,
                                blocks = listOf(created)
                            ),
                            Event.Command.UpdateStructure(
                                context = root,
                                id = root,
                                children = children
                            )
                        )
                    )
                )
            )
        }
    }

    /**
     * Stubs [updateLinkMark] to append the new mark to the given marks — the
     * real use case's merge for a mark that overlaps nothing.
     */
    fun stubUpdateLinkMarksToAppend() {
        updateLinkMark.stub {
            on { invoke(any(), any(), any()) } doAnswer { invocation ->
                val params = invocation.getArgument<UpdateLinkMarks.Params>(1)
                val onResult = invocation
                    .getArgument<(Either<Throwable, List<Block.Content.Text.Mark>>) -> Unit>(2)
                onResult(Either.Right(params.marks + params.newMark))
            }
        }
    }

    fun stubSplitBlock() {
        splitBlock.stub {
            onBlocking {
                invoke(any())
            } doReturn Either.Right(
                Pair(
                    MockDataFactory.randomUuid(),
                    Payload(context = root, events = emptyList())
                )
            )
        }
    }

    fun stubClosePage() {
        closePage.stub {
            onBlocking { async(any()) } doReturn Resultat.success(Unit)
        }
    }

    fun stubUpdateBackground() {
        updateBackgroundColor.stub {
            onBlocking {
                invoke(any())
            } doReturn Either.Right(
                Payload(context = root, events = emptyList())
            )
        }
    }

    fun stubUpdateTextColor() {
        updateTextColor.stub {
            onBlocking {
                invoke(any())
            } doReturn Either.Right(
                Payload(context = root, events = emptyList())
            )
        }
    }

    fun stubUpdateBlocksMark() {
        updateBlocksMark.stub {
            onBlocking {
                invoke(any())
            } doReturn Either.Right(
                Payload(context = root, events = emptyList())
            )
        }
    }

    fun stubDuplicateBlock(
        new: Id,
        events: List<Event>
    ) {
        duplicateBlock.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Pair(
                    listOf(new),
                    Payload(
                        context = root,
                        events = events
                    )
                )
            )
        }
    }

    fun stubSearchObjects(objects: List<ObjectWrapper.Basic> = emptyList()) {
        searchObjects.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(objects)
        }
    }

    fun stubGetDefaultObjectType(
        id: String = MockDataFactory.randomString(),
        type: String = MockDataFactory.randomString(),
        name: String? = null,
        template: String? = null
    ) {
        getDefaultObjectType.stub {
            onBlocking { async(
                params = SpaceId(defaultSpace)
            ) } doReturn Resultat.success(
                GetDefaultObjectType.Response(
                    id = TypeId(id),
                    type = TypeKey(type),
                    name = name,
                    defaultTemplate = template
                )
            )
        }
    }

    fun stubCreateObjectAsMentionOrLink(name: String, id: String) {
        val result = CreateObjectAsMentionOrLink.Result(id, name)
        createObjectAsMentionOrLink.stub {
            onBlocking { async(any()) } doReturn Resultat.success(result)
        }
    }

    protected fun stubGetTemplates(
        templates: List<ObjectWrapper.Basic> = emptyList()
    ) {
        getTemplates.stub {
            onBlocking { run(any()) } doReturn templates
        }
    }

    fun stubFillRow() {
        fillTableRow.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Payload(
                    context = root,
                    events = listOf()
                )
            )
        }
    }

    fun stubGetObjectTypes(types: List<ObjectWrapper.Type>) {
        getObjectTypes.stub {
            onBlocking {
                async(any())
            } doReturn Resultat.success(types)
        }
    }

    fun setStoreOfObjectTypes(types: List<ObjectWrapper.Type>) {
        runBlocking {
            storeOfObjectTypes.clear()
            storeOfObjectTypes.merge(types = types)
        }
    }

    fun stubFileLimitEvents() {
        interceptFileLimitEvents.stub {
            onBlocking { run(Unit) } doReturn emptyFlow()
        }
    }

    fun stubSetInternalFlags() {
        setObjectInternalFlags.stub {
            onBlocking { async(any()) } doReturn Resultat.success(Payload(root, emptyList()))
        }
    }

    fun stubSpaceManager(space: String = defaultSpace) {
        spaceManager.stub {
            onBlocking { get() } doReturn space
        }
    }

    fun stubUserPermission(
        space: SpaceId = SpaceId(defaultSpace),
        permission: SpaceMemberPermissions = SpaceMemberPermissions.OWNER
    ) {
        (permissions as UserPermissionProviderStub).stubObserve(
            space, permission
        )
    }

    fun stubAnalyticSpaceHelperDelegate() {
        Mockito.`when`(analyticSpaceHelperDelegate.provideParams(defaultSpace))
            .thenReturn(AnalyticSpaceHelperDelegate.Params.EMPTY)

        analyticSpaceHelperDelegate.stub {
            on { provideParams(defaultSpace) } doReturn AnalyticSpaceHelperDelegate.Params.EMPTY
        }
    }

    @Mock
    lateinit var stringResourceProvider: StringResourceProvider

    fun proceedWithDefaultBeforeTestStubbing() {
        fieldParser = FieldParserImpl(dateProvider, logger, getDateObjectByTimestamp, stringResourceProvider)
        stubAnalyticSpaceHelperDelegate()
        stubSpaceManager()
        stubUserPermission()
        stubGetNetworkMode()
        stubFileLimitEvents()
        stubInterceptEvents()
        stubClosePage()
        stubInterceptThreadStatus()
        val spaceView = StubSpaceView(
            id = defaultSpace,
            spaceUxType = SpaceUxType.DATA
        )
        spacedViews.stub {
            onBlocking { get(space = SpaceId(defaultSpace)) } doReturn spaceView
        }
    }
}