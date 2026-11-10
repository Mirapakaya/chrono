package com.chronoio.chrono.features.editor.base

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import com.chronoio.chrono.R
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.app.DefaultFeatureToggles
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Event
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectViewDetails
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Relation
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_utils.tools.FeatureToggles
import com.chronoio.chrono.domain.auth.interactor.ClearLastOpenedObject
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
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
import com.chronoio.chrono.domain.clipboard.Clipboard
import com.chronoio.chrono.domain.clipboard.Copy
import com.chronoio.chrono.domain.clipboard.Paste
import com.chronoio.chrono.domain.config.Gateway
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.cover.RemoveDocCover
import com.chronoio.chrono.domain.cover.SetDocCoverImage
import com.chronoio.chrono.domain.download.DownloadFile
import com.chronoio.chrono.domain.event.interactor.InterceptEvents
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.icon.SetDocumentImageIcon
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.misc.UrlBuilderImpl
import com.chronoio.chrono.presentation.util.UrlHelper
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.networkmode.GetNetworkMode
import com.chronoio.chrono.domain.`object`.ConvertObjectToCollection
import com.chronoio.chrono.domain.`object`.ConvertObjectToSet
import com.chronoio.chrono.domain.`object`.UpdateDetail
import com.chronoio.chrono.domain.objects.DefaultStoreOfObjectTypes
import com.chronoio.chrono.domain.objects.DefaultStoreOfRelations
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
import com.chronoio.chrono.domain.relations.AddRelationToObject
import com.chronoio.chrono.domain.relations.SetRelationKey
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.sets.FindObjectSetForType
import com.chronoio.chrono.domain.table.CreateTable
import com.chronoio.chrono.domain.table.FillTableRow
import com.chronoio.chrono.domain.templates.ApplyTemplate
import com.chronoio.chrono.domain.templates.GetTemplates
import com.chronoio.chrono.domain.unsplash.DownloadUnsplashImage
import com.chronoio.chrono.domain.unsplash.UnsplashRepository
import com.chronoio.chrono.domain.workspace.FileLimitsEventChannel
import com.chronoio.chrono.domain.workspace.InterceptFileLimitEvents
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.common.Delegator
import com.chronoio.chrono.presentation.editor.DocumentExternalEventReducer
import com.chronoio.chrono.presentation.editor.Editor
import com.chronoio.chrono.presentation.editor.EditorViewModel
import com.chronoio.chrono.presentation.editor.EditorViewModelFactory
import com.chronoio.chrono.presentation.editor.cover.CoverImageHashProvider
import com.chronoio.chrono.presentation.editor.editor.Interactor
import com.chronoio.chrono.presentation.editor.editor.Orchestrator
import com.chronoio.chrono.presentation.editor.editor.Proxy
import com.chronoio.chrono.presentation.editor.editor.pattern.DefaultPatternMatcher
import com.chronoio.chrono.presentation.editor.editor.table.EditorTableDelegate
import com.chronoio.chrono.presentation.editor.render.DefaultBlockViewRenderer
import com.chronoio.chrono.presentation.editor.selection.SelectionStateHolder
import com.chronoio.chrono.presentation.editor.toggle.ToggleStateHolder
import com.chronoio.chrono.presentation.search.ObjectSearchConstants
import com.chronoio.chrono.presentation.templates.ObjectTypeTemplatesContainer
import com.chronoio.chrono.presentation.util.CopyFileToCacheDirectory
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.util.downloader.DocumentFileShareDownloader
import com.chronoio.chrono.presentation.widgets.collection.ResourceProvider
import com.chronoio.chrono.test_utils.MockDataFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

open class EditorTestSetup {

    lateinit var createBlockLinkWithObject: CreateBlockLinkWithObject
    lateinit var downloadFile: DownloadFile
    lateinit var undo: Undo
    lateinit var redo: Redo
    lateinit var copy: Copy
    lateinit var paste: Paste
    lateinit var updateAlignment: UpdateAlignment
    lateinit var replaceBlock: ReplaceBlock
    lateinit var setupBookmark: SetupBookmark
    lateinit var createBookmarkBlock: CreateBookmarkBlock
    lateinit var uploadBlock: UploadBlock
    lateinit var splitBlock: SplitBlock
    lateinit var updateBackgroundColor: UpdateBackgroundColor
    lateinit var move: Move
    lateinit var setRelationKey: SetRelationKey
    lateinit var updateDetail: UpdateDetail

    lateinit var copyFileToCacheDirectory: CopyFileToCacheDirectory

    @Mock
    lateinit var documentFileShareDownloader: DocumentFileShareDownloader

    @Mock
    lateinit var dateProvider: DateProvider

    @Mock
    lateinit var openPage: OpenPage

    @Mock
    lateinit var closePage: CloseObject

    @Mock
    lateinit var updateText: UpdateText

    @Mock
    lateinit var createBlock: CreateBlock

    @Mock
    lateinit var interceptEvents: InterceptEvents

    @Mock
    lateinit var updateCheckbox: UpdateCheckbox

    @Mock
    lateinit var unlinkBlocks: UnlinkBlocks

    lateinit var getSearchObjects: SearchObjects

    @Mock
    lateinit var duplicateBlock: DuplicateBlock

    @Mock
    lateinit var updateTextStyle: UpdateTextStyle

    @Mock
    lateinit var updateTextColor: UpdateTextColor

    @Mock
    lateinit var updateLinkMarks: UpdateLinkMarks

    @Mock
    lateinit var removeLinkMark: RemoveLinkMark

    @Mock
    lateinit var mergeBlocks: MergeBlocks

    @Mock
    lateinit var createObject: CreateObject

    @Mock
    lateinit var appCoroutineDispatchers: AppCoroutineDispatchers

    @Mock
    lateinit var storelessSubscriptionContainer: StorelessSubscriptionContainer

    lateinit var getTemplates: GetTemplates
    lateinit var applyTemplate: ApplyTemplate

    lateinit var createObjectAsMentionOrLink: CreateObjectAsMentionOrLink

    lateinit var setDocCoverImage: SetDocCoverImage
    lateinit var setDocImageIcon: SetDocumentImageIcon
    lateinit var removeDocCover: RemoveDocCover

    lateinit var updateFields: UpdateFields
    lateinit var turnIntoDocument: TurnIntoDocument
    lateinit var turnIntoStyle: TurnIntoStyle
    lateinit var setObjectType: SetObjectType
    lateinit var objectToSet: ConvertObjectToSet
    lateinit var clearBlockContent: ClearBlockContent
    lateinit var clearBlockStyle: ClearBlockStyle

    lateinit var getDefaultObjectType: GetDefaultObjectType

    private lateinit var findObjectSetForType: FindObjectSetForType
    private lateinit var createObjectSet: CreateObjectSet
    lateinit var getObjectTypes: GetObjectTypes

    lateinit var downloadUnsplashImage: DownloadUnsplashImage

    lateinit var featureToggles: FeatureToggles

    @Mock
    lateinit var updateDivider: UpdateDivider

    @Mock
    lateinit var clearLastOpenedObject: ClearLastOpenedObject

    @Mock
    lateinit var uriMatcher: Clipboard.UriMatcher

    @Mock
    lateinit var updateBlocksMark: UpdateBlocksMark

    @Mock
    lateinit var repo: BlockRepository

    @Mock
    lateinit var getNetworkMode: GetNetworkMode

    @Mock
    lateinit var unsplashRepository: UnsplashRepository

    @Mock
    lateinit var userSettingsRepository: UserSettingsRepository

    @Mock
    lateinit var coverImageHashProvider: CoverImageHashProvider

    @Mock
    lateinit var clipboard: Clipboard

    @Mock
    lateinit var gateway: Gateway

    @Mock
    lateinit var analytics: Analytics

    @Mock
    lateinit var createTable: CreateTable

    @Mock
    lateinit var fillTableRow: FillTableRow

    @Mock
    lateinit var tableDelegate: EditorTableDelegate

    @Mock
    lateinit var objectToCollection: ConvertObjectToCollection

    @Mock
    lateinit var fileLimitsEventChannel: FileLimitsEventChannel

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var templatesContainer: ObjectTypeTemplatesContainer

    @Mock
    lateinit var permissions: UserPermissionProvider

    @Mock
    lateinit var analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate

    @Mock
    lateinit var spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider

    @Mock
    lateinit var fieldParser: FieldParser

    @Mock
    lateinit var resourceProvider: ResourceProvider

    lateinit var interceptFileLimitEvents: InterceptFileLimitEvents

    lateinit var addRelationToObject: AddRelationToObject

    val root: String = "rootId123"
    val defaultSpace = MockDataFactory.randomString()


    @Mock
    lateinit var urlHelper: UrlHelper

    private val urlBuilder by lazy {
        UrlBuilderImpl(gateway = gateway)
    }

    private val intents = Proxy.Intents()

    private val stores = Editor.Storage()

    private val proxies = Editor.Proxer(
        intents = intents
    )

    @Mock
    lateinit var spacedViews: SpaceViewSubscriptionContainer

    private val storeOfRelations: StoreOfRelations = DefaultStoreOfRelations()
    private val storeOfObjectTypes: StoreOfObjectTypes = DefaultStoreOfObjectTypes()

    open fun setup() {
        MockitoAnnotations.openMocks(this)

        val dispatchers = AppCoroutineDispatchers(
            io = StandardTestDispatcher(),
            main = StandardTestDispatcher(),
            computation = StandardTestDispatcher()
        )

        splitBlock = SplitBlock(repo)
        undo = Undo(repo, dispatchers)
        redo = Redo(repo, dispatchers)
        objectToSet = ConvertObjectToSet(repo, dispatchers)
        replaceBlock = ReplaceBlock(repo)
        setupBookmark = SetupBookmark(repo)
        updateAlignment = UpdateAlignment(repo)
        uploadBlock = UploadBlock(repo)
        createBlockLinkWithObject = CreateBlockLinkWithObject(repo, dispatchers)
        setRelationKey = SetRelationKey(repo)
        turnIntoDocument = TurnIntoDocument(repo)
        updateFields = UpdateFields(repo)
        setObjectType = SetObjectType(repo, dispatchers)
        createObjectAsMentionOrLink = CreateObjectAsMentionOrLink(
            repo = repo,
            dispatchers = dispatchers,
            spaceManager = spaceManager
        )
        getSearchObjects = SearchObjects(repo)
        downloadUnsplashImage = DownloadUnsplashImage(unsplashRepository)
        clearBlockContent = ClearBlockContent(repo)
        clearBlockStyle = ClearBlockStyle(repo)
        downloadFile = DownloadFile(
            downloader = mock(),
            context = Dispatchers.Main
        )
        copy = Copy(
            repo = repo,
            clipboard = clipboard
        )

        paste = Paste(
            repo = repo,
            clipboard = clipboard,
            matcher = uriMatcher
        )
        move = Move(
            repo, dispatchers
        )
        getObjectTypes = GetObjectTypes(repo, dispatchers)

        updateBackgroundColor = UpdateBackgroundColor(repo)

        setDocCoverImage = SetDocCoverImage(repo)
        setDocImageIcon = SetDocumentImageIcon(repo)
        removeDocCover = RemoveDocCover(repo)
        turnIntoStyle = TurnIntoStyle(repo)
        updateDetail = UpdateDetail(repo)
        getDefaultObjectType = GetDefaultObjectType(
            userSettingsRepository = userSettingsRepository,
            blockRepository = repo,
            dispatchers = dispatchers
        )
        createObjectSet = CreateObjectSet(repo)
        findObjectSetForType = FindObjectSetForType(repo)
        createBookmarkBlock = CreateBookmarkBlock(repo)
        applyTemplate = ApplyTemplate(
            repo = repo,
            dispatchers = dispatchers
        )
        getTemplates = GetTemplates(
            repo = repo,
            dispatchers = dispatchers,
            spaceManager = spaceManager
        )

        featureToggles = mock<DefaultFeatureToggles>()
        addRelationToObject = AddRelationToObject(repo, dispatchers)

        interceptFileLimitEvents = InterceptFileLimitEvents(fileLimitsEventChannel, dispatchers)

        TestEditorFragment.testViewModelFactory = EditorViewModelFactory(
            openPage = openPage,
            closeObject = closePage,
            interceptEvents = interceptEvents,
            updateLinkMarks = updateLinkMarks,
            removeLinkMark = removeLinkMark,
            createBlockLinkWithObject = createBlockLinkWithObject,
            documentEventReducer = DocumentExternalEventReducer(),
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
            orchestrator = Orchestrator(
                createBlock = createBlock,
                splitBlock = splitBlock,
                unlinkBlocks = unlinkBlocks,
                updateCheckbox = updateCheckbox,
                updateTextStyle = updateTextStyle,
                updateText = updateText,
                updateBackgroundColor = updateBackgroundColor,
                undo = undo,
                redo = redo,
                copy = copy,
                paste = paste,
                duplicateBlock = duplicateBlock,
                updateAlignment = updateAlignment,
                downloadFile = downloadFile,
                documentFileShareDownloader = documentFileShareDownloader,
                mergeBlocks = mergeBlocks,
                updateTextColor = updateTextColor,
                replaceBlock = replaceBlock,
                setupBookmark = setupBookmark,
                setRelationKey = setRelationKey,
                memory = Editor.Memory(
                    selections = SelectionStateHolder.Default()
                ),
                stores = stores,
                proxies = proxies,
                textInteractor = Interactor.TextInteractor(
                    proxies = proxies,
                    stores = stores,
                    matcher = DefaultPatternMatcher()
                ),
                uploadBlock = uploadBlock,
                move = move,
                analytics = analytics,
                updateDivider = updateDivider,
                updateFields = updateFields,
                turnIntoDocument = turnIntoDocument,
                turnIntoStyle = turnIntoStyle,
                updateBlocksMark = updateBlocksMark,
                createBookmarkBlock = createBookmarkBlock,
                createTable = createTable,
                fillTableRow = fillTableRow,
                clearBlockContent = clearBlockContent,
                clearBlockStyle = clearBlockStyle,
                analyticsSpaceHelperDelegate = analyticSpaceHelperDelegate,
                spaceManager = spaceManager
            ),
            createObjectAsMentionOrLink = createObjectAsMentionOrLink,
            analytics = analytics,
            dispatcher = Dispatcher.Default(),
            updateDetail = updateDetail,
            searchObjects = getSearchObjects,
            getDefaultObjectType = getDefaultObjectType,
            createObjectSet = createObjectSet,
            findObjectSetForType = findObjectSetForType,
            copyFileToCacheDirectory = copyFileToCacheDirectory,
            downloadUnsplashImage = downloadUnsplashImage,
            delegator = Delegator.Default(),
            setDocCoverImage = setDocCoverImage,
            setDocImageIcon = setDocImageIcon,
            createObject = createObject,
            objectToSet = objectToSet,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            featureToggles = featureToggles,
            tableDelegate = tableDelegate,
            getObjectTypes = getObjectTypes,
            objectToCollection = objectToCollection,
            interceptFileLimitEvents = interceptFileLimitEvents,
            addRelationToObject = addRelationToObject,
            spaceManager = spaceManager,
            applyTemplate = applyTemplate,
            setObjectType = setObjectType,
            templatesContainer = templatesContainer,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            dispatchers = appCoroutineDispatchers,
            params = EditorViewModel.Params(
                ctx = root,
                space = SpaceId(defaultSpace)
            ),
            permissions = permissions,
            syncStatusProvider = spaceSyncAndP2PStatusProvider,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            clearLastOpenedObject = clearLastOpenedObject,
            getNetworkMode = getNetworkMode,
            fieldParser = fieldParser,
            dateProvider = dateProvider,
            spaceViews = spacedViews,
            urlHelper = urlHelper,
            addDiscussion = mock(),
            getChatMessages = mock(),
            backHistoryDelegate = mock(),
            exitToVaultDelegate = mock()
        )
    }

    /**
     * STUBBING
     */

    fun stubInterceptEvents() {
        interceptEvents.stub {
            onBlocking { build(any()) } doReturn emptyFlow()
        }
    }

    fun stubInterceptThreadStatus() {
        spaceSyncAndP2PStatusProvider.stub {
            onBlocking { observe() } doReturn emptyFlow()
        }
    }

    fun stubOpenDocument(
        document: List<Block>,
        details: ObjectViewDetails = ObjectViewDetails.EMPTY,
        relations: List<Relation> = emptyList()
    ) {
        openPage.stub {
            onBlocking { execute(any()) } doReturn Resultat.success(
                Result.Success(
                    Payload(
                        context = root,
                        events = listOf(
                            Event.Command.ShowObject(
                                context = root,
                                root = root,
                                details = details.details,
                                blocks = document,
                            )
                        )
                    )
                )
            )
        }
    }

    fun stubCreateBlock(
        params: CreateBlock.Params,
        events: List<Event.Command>
    ) {
        createBlock.stub {
            onBlocking { execute(params) } doReturn Resultat.success(
                Pair(
                    MockDataFactory.randomUuid(),
                    Payload(context = root, events = events)
                )
            )
        }
    }

    fun stubSplitBlocks(
        command: Command.Split,
        new: Id,
        events: List<Event.Command>
    ) {
        repo.stub {
            onBlocking {
                split(command = command)
            } doReturn Pair(new, Payload(context = root, events = events))
        }
    }

    fun stubUpdateText() {
        updateText.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(Unit)
        }
    }

    fun stubGetObjectTypes(objectTypes: List<ObjectWrapper.Type>) {
        repo.stub {
            onBlocking {
                searchObjects(
                    space = SpaceId(defaultSpace),
                    filters = ObjectSearchConstants.filterTypes(),
                    keys = ObjectSearchConstants.defaultKeysObjectType,
                    sorts = emptyList(),
                    limit = 0,
                    offset = 0,
                    fulltext = ""
                )
            } doReturn objectTypes.map { it.map }
        }
    }

    fun stubUpdateTextStyle(
        events: List<Event> = emptyList()
    ) {
        updateTextStyle.stub {
            onBlocking { invoke(any()) } doReturn Either.Right(
                Payload(
                    context = root,
                    events = events
                )
            )
        }
    }

    fun stubAnalytics() {
        analytics.stub {
            onBlocking { registerEvent(any()) } doReturn Unit
        }
    }

    fun launch(args: Bundle): FragmentScenario<TestEditorFragment> {
        return launchFragmentInContainer(
            fragmentArgs = args,
            themeResId = R.style.AppTheme
        )
    }
}