package com.chronoio.chrono.features.sets.dv

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import com.chronoio.chrono.R
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Event
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectViewDetails
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Relation
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.SearchResult
import com.chronoio.chrono.core_models.Struct
import com.chronoio.chrono.core_models.SubscriptionEvent
import com.chronoio.chrono.core_models.multiplayer.SpaceMemberPermissions
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.device.providers.AppDefaultDateFormatProviderImpl
import com.chronoio.chrono.device.providers.DateProviderImpl
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.Result
import com.chronoio.chrono.domain.block.interactor.UpdateText
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.collections.AddObjectToCollection
import com.chronoio.chrono.domain.collections.RemoveObjectFromCollection
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.Gateway
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.cover.SetDocCoverImage
import com.chronoio.chrono.domain.dataview.SetDataViewProperties
import com.chronoio.chrono.domain.dataview.interactor.SetDataViewObjectOrder
import com.chronoio.chrono.domain.dataview.SetDataViewQuery
import com.chronoio.chrono.domain.dataview.interactor.CreateDataViewObject
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.domain.event.interactor.InterceptEvents
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.discussions.AddDiscussion
import com.chronoio.chrono.domain.media.UploadFile
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.misc.UrlBuilderImpl
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.ConvertObjectToCollection
import com.chronoio.chrono.domain.`object`.DuplicateObjects
import com.chronoio.chrono.domain.`object`.UpdateDetail
import com.chronoio.chrono.domain.objects.DefaultObjectStore
import com.chronoio.chrono.domain.objects.DefaultStoreOfRelations
import com.chronoio.chrono.domain.objects.ObjectStore
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.objects.options.GetOptions
import com.chronoio.chrono.domain.page.CloseObject
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.BoardGroupSubscriptionContainer
import com.chronoio.chrono.domain.search.BoardRecordsSubscriptionContainer
import com.chronoio.chrono.domain.search.DataViewSubscriptionContainer
import com.chronoio.chrono.domain.search.SubscriptionEventChannel
import com.chronoio.chrono.domain.sets.OpenObjectSet
import com.chronoio.chrono.domain.sets.SetQueryToObjectSet
import com.chronoio.chrono.domain.templates.CreateTemplate
import com.chronoio.chrono.domain.templates.GetTemplates
import com.chronoio.chrono.domain.unsplash.DownloadUnsplashImage
import com.chronoio.chrono.domain.unsplash.UnsplashRepository
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.emojifier.data.DefaultDocumentEmojiIconProvider
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.common.Action
import com.chronoio.chrono.presentation.common.Delegator
import com.chronoio.chrono.presentation.editor.cover.CoverImageHashProvider
import com.chronoio.chrono.presentation.navigation.backstack.BackHistoryMenuState
import com.chronoio.chrono.presentation.sets.ObjectSetDatabase
import com.chronoio.chrono.presentation.sets.ObjectSetPaginator
import com.chronoio.chrono.presentation.sets.ObjectSetSession
import com.chronoio.chrono.presentation.sets.ObjectSetViewModel
import com.chronoio.chrono.presentation.sets.ObjectSetViewModelFactory
import com.chronoio.chrono.presentation.sets.state.DefaultObjectStateReducer
import com.chronoio.chrono.presentation.sets.subscription.DefaultDataViewSubscription
import com.chronoio.chrono.presentation.sets.viewer.ViewerDelegate
import com.chronoio.chrono.presentation.templates.ObjectTypeTemplatesContainer
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.test_utils.MockDataFactory
import java.time.ZoneId
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.stub

abstract class TestObjectSetSetup {

    private lateinit var openObjectSet: OpenObjectSet
    private lateinit var updateDataViewViewer: UpdateDataViewViewer
    private lateinit var setObjectDetails: UpdateDetail
    private lateinit var updateText: UpdateText
    private lateinit var createDataViewObject: CreateDataViewObject
    private lateinit var closeObject: CloseObject
    private lateinit var setDocCoverImage: SetDocCoverImage
    private lateinit var downloadUnsplashImage: DownloadUnsplashImage
    private lateinit var setDataViewQuery: SetDataViewQuery

    @Mock
    lateinit var deepLinkResolver: DeepLinkResolver

    protected val defaultSpace: Id = MockDataFactory.randomString()

    lateinit var urlBuilder: UrlBuilder

    @Mock
    lateinit var coverImageHashProvider: CoverImageHashProvider

    @Mock
    lateinit var permissions: UserPermissionProvider

    @Mock
    lateinit var repo: BlockRepository

    @Mock
    lateinit var unsplashRepo: UnsplashRepository

    @Mock
    lateinit var auth: AuthRepository

    @Mock
    lateinit var settingsRepository: UserSettingsRepository

    @Mock
    lateinit var userSettingsRepository: UserSettingsRepository

    @Mock
    lateinit var gateway: Gateway
    @Mock
    lateinit var interceptEvents: InterceptEvents
    @Mock
    lateinit var spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider
    @Mock
    lateinit var subscriptionEventChannel: SubscriptionEventChannel
    @Mock
    lateinit var analytics: Analytics
    lateinit var convertObjectToCollection: ConvertObjectToCollection
    lateinit var setQueryToObjectSet: SetQueryToObjectSet

    lateinit var addObjectToCollection: AddObjectToCollection

    lateinit var createObject: CreateObject

    @Mock
    lateinit var storeOfObjectTypes: StoreOfObjectTypes

    lateinit var duplicateObjects: DuplicateObjects

    lateinit var setObjectListIsArchived: SetObjectListIsArchived

    @Mock
    lateinit var templatesContainer: ObjectTypeTemplatesContainer

    @Mock
    lateinit var viewerDelegate: ViewerDelegate

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var configStorage: ConfigStorage


    lateinit var createTemplate: CreateTemplate

    @Mock
    lateinit var storelessSubscriptionContainer: StorelessSubscriptionContainer



    @Mock
    lateinit var analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate


    lateinit var removeObjectFromCollection: RemoveObjectFromCollection

    @Mock
    lateinit var spacedViews: SpaceViewSubscriptionContainer

    private lateinit var getTemplates: GetTemplates
    private lateinit var getDefaultObjectType: GetDefaultObjectType

    private val session = ObjectSetSession()
    private val dispatcher: Dispatcher<Payload> = Dispatcher.Default()
    private val paginator = ObjectSetPaginator()
    private val store: ObjectStore = DefaultObjectStore()
    private val storeOfRelations: StoreOfRelations = DefaultStoreOfRelations()

    private lateinit var database: ObjectSetDatabase
    private lateinit var dataViewSubscriptionContainer: DataViewSubscriptionContainer

    val ctx : Id = MockDataFactory.randomUuid()

    abstract val title : Block

    val header get() = Block(
        id = MockDataFactory.randomUuid(),
        content = Block.Content.Layout(
            type = Block.Content.Layout.Type.HEADER
        ),
        fields = Block.Fields.empty(),
        children = listOf(title.id)
    )

    private val sourceTypeId = MockDataFactory.randomUuid()

    val defaultDetails get() = ObjectViewDetails(
        mapOf(
            ctx to
                    mapOf(
                        Relations.ID to ctx,
                        Relations.NAME to (title.content as Block.Content.Text).text,
                        Relations.LAYOUT to ObjectType.Layout.SET.code.toDouble(),
                        Relations.SET_OF to listOf(sourceTypeId),
                        Relations.ICON_EMOJI to DefaultDocumentEmojiIconProvider.DOCUMENT_SET.random()
                    ),
            sourceTypeId to mapOf(
                Relations.ID to sourceTypeId,
                Relations.NAME to "Fixture type",
                Relations.LAYOUT to ObjectType.Layout.OBJECT_TYPE.code.toDouble()
            )
        )
    )

    private val objectStore: ObjectStore = store

    private val delegator = Delegator.Default<Action>()

    @Mock
    lateinit var localeProvider : LocaleProvider


    @Mock
    lateinit var fieldParser: FieldParser

    private val stringResourceProvider: StringResourceProvider =
        mock(StringResourceProvider::class.java)

    private val dateProvider by lazy { DateProviderImpl(
        defaultZoneId = ZoneId.systemDefault(),
        localeProvider = localeProvider,
        appDefaultDateFormatProvider = AppDefaultDateFormatProviderImpl(localeProvider),
        stringResourceProvider = stringResourceProvider
    ) }

    open fun setup() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(localeProvider.locale()).thenReturn(Locale.getDefault())
        userSettingsRepository.stub {
            on { observeKanbanEnabled() } doReturn flowOf(true)
        }
        permissions.stub {
            on { observe(SpaceId(defaultSpace)) } doReturn flowOf(SpaceMemberPermissions.OWNER)
        }
        fieldParser.stub {
            on { getObjectName(any<ObjectWrapper.Basic>(), any()) } doReturn "Fixture record"
        }
        analyticSpaceHelperDelegate.stub {
            on { provideParams(any()) } doReturn AnalyticSpaceHelperDelegate.Params.EMPTY
        }
        deepLinkResolver = mock()
        val dispatchers = AppCoroutineDispatchers(
            io = Dispatchers.IO,
            main = Dispatchers.Main,
            computation = Dispatchers.Default
        )
        val createTypes = org.mockito.kotlin.mock<StoreOfObjectTypes> {
            on { observe() } doReturn emptyFlow()
        }
        val createSpaces = org.mockito.kotlin.mock<SpaceViewSubscriptionContainer> {
            on { observe<Boolean>(SpaceId(eq(defaultSpace)), any(), any()) } doReturn emptyFlow()
        }
        TestObjectSetFragment.testCreateObjectFactory =
            com.chronoio.chrono.feature_create_object.presentation.CreateObjectViewModelFactory(
                storeOfObjectTypes = createTypes,
                spaceViewContainer = createSpaces,
                uploadFile = UploadFile(repo, dispatchers),
                fileSharer = org.mockito.kotlin.mock(),
                vmParams = com.chronoio.chrono.feature_create_object.presentation.NewCreateObjectViewModel.VmParams(
                    spaceId = SpaceId(defaultSpace), showAttachObject = false, showMediaSection = true
                )
            )

        setDataViewQuery = SetDataViewQuery(repo)
        updateText = UpdateText(repo)
        openObjectSet = OpenObjectSet(repo, userSettingsRepository)
        getDefaultObjectType = GetDefaultObjectType(
            userSettingsRepository = userSettingsRepository,
            blockRepository = repo,
            dispatchers = dispatchers
        )
        convertObjectToCollection = ConvertObjectToCollection(repo, dispatchers)
        setQueryToObjectSet = SetQueryToObjectSet(repo, dispatchers)
        addObjectToCollection = AddObjectToCollection(repo, dispatchers)
        duplicateObjects = DuplicateObjects(repo, dispatchers)
        setObjectListIsArchived = SetObjectListIsArchived(repo, dispatchers)
        createTemplate = CreateTemplate(repo, dispatchers)
        removeObjectFromCollection = RemoveObjectFromCollection(repo, dispatchers)
        createObject = CreateObject(repo, getDefaultObjectType, dispatchers)
        createDataViewObject = CreateDataViewObject(
            repo = repo,
            dispatchers = dispatchers,
            spaceManager = spaceManager
        )
        setObjectDetails = UpdateDetail(repo)
        updateDataViewViewer = UpdateDataViewViewer(repo, dispatchers)
        closeObject = CloseObject(repo, dispatchers)
        urlBuilder = UrlBuilderImpl(gateway)
        downloadUnsplashImage = DownloadUnsplashImage(unsplashRepo)
        setDocCoverImage = SetDocCoverImage(repo)
        getTemplates = GetTemplates(
            repo = repo,
            dispatchers = dispatchers,
            spaceManager = spaceManager
        )
        database = ObjectSetDatabase(store)
        dataViewSubscriptionContainer = DataViewSubscriptionContainer(
            repo = repo,
            store = store,
            channel = subscriptionEventChannel,
            dispatchers = dispatchers
        )
        TestObjectSetFragment.testVmFactory = ObjectSetViewModelFactory(
            openObjectSet = openObjectSet,
            closeObject = closeObject,
            interceptEvents = interceptEvents,
            createDataViewObject = createDataViewObject,
            setObjectDetails = setObjectDetails,
            updateText = updateText,
            urlBuilder = urlBuilder,
            coverImageHashProvider = coverImageHashProvider,
            session = session,
            dispatcher = dispatcher,
            analytics = analytics,
            downloadUnsplashImage = downloadUnsplashImage,
            setDocCoverImage = setDocCoverImage,
            delegator = delegator,
            createObject = createObject,
            paginator = paginator,
            database = database,
            dataViewSubscriptionContainer = dataViewSubscriptionContainer,
            storeOfRelations = storeOfRelations,
            objectStateReducer = DefaultObjectStateReducer(),
            dataViewSubscription = DefaultDataViewSubscription(dataViewSubscriptionContainer),
            objectToCollection = convertObjectToCollection,
            setQueryToObjectSet = setQueryToObjectSet,
            objectStore = objectStore,
            addObjectToCollection = addObjectToCollection,
            storeOfObjectTypes = storeOfObjectTypes,
            templatesContainer = templatesContainer,
            setObjectListIsArchived = setObjectListIsArchived,
            duplicateObjects = duplicateObjects,
            viewerDelegate = viewerDelegate,
            spaceManager = spaceManager,
            createTemplate = createTemplate,
            dateProvider = dateProvider,
            params = ObjectSetViewModel.Params(
                ctx = ctx,
                space = SpaceId(defaultSpace)
            ),
            permissions = permissions,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            spaceSyncAndP2PStatusProvider = spaceSyncAndP2PStatusProvider,
            fieldParser = fieldParser,
            deepLinkResolver = deepLinkResolver,
            spaceViews = spacedViews,
            removeObjectFromCollection = removeObjectFromCollection,
            setDataViewProperties = SetDataViewProperties(repo, dispatchers),
            setDataViewObjectOrder = SetDataViewObjectOrder(repo, dispatchers),
            boardGroupSubscriptionContainer = BoardGroupSubscriptionContainer(
                repo, subscriptionEventChannel, dispatchers, mock()
            ),
            createBlock = mock(),
            emojiProvider = mock(),
            emojiSuggester = mock(),
            stringResourceProvider = stringResourceProvider,
            getDefaultObjectType = getDefaultObjectType,
            addDiscussion = AddDiscussion(repo, dispatchers),
            backHistoryDelegate = org.mockito.kotlin.mock {
                on { backHistoryMenu } doReturn MutableStateFlow(BackHistoryMenuState.Hidden)
            },
            exitToVaultDelegate = org.mockito.kotlin.mock(),
            boardRecordsSubscriptionContainer = BoardRecordsSubscriptionContainer(
                repo, subscriptionEventChannel, store, dispatchers, mock()
            ),
            userSettingsRepository = userSettingsRepository,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
        )

        Mockito.`when`(localeProvider.locale()).thenReturn(Locale.getDefault())
    }

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

    fun stubOpenObjectSet(
        set: List<Block>,
        details: Map<Id, Struct> = emptyMap<Id, Struct>()
    ) {
        repo.stub {
            onBlocking { openObjectSet(ctx, SpaceId(defaultSpace)) } doReturn Result.Success(
                Payload(
                    context = ctx,
                    events = listOf(
                        Event.Command.ShowObject(
                            context = ctx,
                            root = ctx,
                            details = details,
                            blocks = set,
                        )
                    )
                )
            )
        }
    }

    fun stubOpenObjectSetWithRecord(
        set: List<Block>,
        details: ObjectViewDetails = ObjectViewDetails.EMPTY
    ) {
        repo.stub {
            onBlocking { openObjectSet(ctx, SpaceId(defaultSpace)) } doReturn Result.Success(
                Payload(
                    context = ctx,
                    events = listOf(
                        Event.Command.ShowObject(
                            context = ctx,
                            root = ctx,
                            details = details.details,
                            blocks = set,
                        )
                    )
                )
            )
        }
    }

    fun stubSearchWithSubscription(records: List<ObjectWrapper.Basic> = emptyList()) {
        repo.stub {
            onBlocking {
                // Mockito records matchers in evaluation order, while Kotlin reorders
                // named arguments at the call site. Keep the declaration order here.
                searchObjectsWithSubscription(
                    space = SpaceId(eq(defaultSpace)),
                    subscription = any(),
                    sorts = any(),
                    filters = any(),
                    keys = any(),
                    source = any(),
                    offset = any(),
                    limit = any(),
                    beforeId = anyOrNull(),
                    afterId = anyOrNull(),
                    ignoreWorkspace = anyOrNull(),
                    noDepSubscription = anyOrNull(),
                    collection = anyOrNull()
                )
            } doReturn SearchResult(
                results = records,
                dependencies = emptyList(),
                counter = null
            )
        }
    }

    fun stubSubscriptionEventChannel(
        flow: Flow<List<SubscriptionEvent>> = emptyFlow()
    ) {
        subscriptionEventChannel.stub {
            onBlocking {
                subscribe(listOf(DefaultDataViewSubscription.getDataViewSubscriptionId(ctx)))
            } doReturn flow
        }
    }

    fun stubRelations(relations: List<Relation>) = runBlocking {
        storeOfRelations.merge(relations.map { relation ->
            ObjectWrapper.Relation(mapOf(
                Relations.ID to "relation-${relation.key}",
                Relations.RELATION_KEY to relation.key,
                Relations.NAME to relation.name,
                Relations.RELATION_FORMAT to relation.format.code.toDouble()
            ))
        })
    }

    fun stubSingleBoardCheckboxGroup() {
        repo.stub {
            onBlocking {
                objectGroupsSubscribe(SpaceId(eq(defaultSpace)), any(), any(), any(), any(), anyOrNull())
            } doReturn listOf(com.chronoio.chrono.core_models.DataViewGroup(
                "runtime-unchecked", com.chronoio.chrono.core_models.DataViewGroup.Value.Checkbox(false)))
        }
        subscriptionEventChannel.stub { onBlocking { subscribe(any()) } doReturn emptyFlow() }
        stringResourceProvider.stub { on { getKanbanCheckboxGroupTitle(any()) } doReturn "Unchecked" }
    }

    fun launchFragment(args: Bundle): FragmentScenario<TestObjectSetFragment> {
        return launchFragmentInContainer(
            fragmentArgs = Bundle(args).apply {
                putString(com.chronoio.chrono.ui.sets.ObjectSetFragment.SPACE_ID_KEY, defaultSpace)
            },
            themeResId = R.style.AppTheme
        )
    }
}
