package com.chronoio.chrono.presentation.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.block.interactor.CreateBlock
import com.chronoio.chrono.domain.block.interactor.UpdateText
import com.chronoio.chrono.domain.collections.AddObjectToCollection
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.collections.RemoveObjectFromCollection
import com.chronoio.chrono.domain.cover.SetDocCoverImage
import com.chronoio.chrono.domain.dataview.SetDataViewProperties
import com.chronoio.chrono.domain.dataview.interactor.SetDataViewObjectOrder
import com.chronoio.chrono.domain.search.BoardGroupSubscriptionContainer
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.search.BoardRecordsSubscriptionContainer
import com.chronoio.chrono.domain.discussions.AddDiscussion
import com.chronoio.chrono.domain.dataview.interactor.CreateDataViewObject
import com.chronoio.chrono.domain.event.interactor.InterceptEvents
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.misc.DeepLinkResolver
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.`object`.ConvertObjectToCollection
import com.chronoio.chrono.domain.`object`.DuplicateObjects
import com.chronoio.chrono.domain.`object`.UpdateDetail
import com.chronoio.chrono.domain.objects.ObjectStore
import com.chronoio.chrono.domain.objects.SetObjectListIsArchived
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.page.CloseObject
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.domain.search.DataViewSubscriptionContainer
import com.chronoio.chrono.domain.sets.OpenObjectSet
import com.chronoio.chrono.domain.sets.SetQueryToObjectSet
import com.chronoio.chrono.domain.templates.CreateTemplate
import com.chronoio.chrono.domain.unsplash.DownloadUnsplashImage
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.emojifier.data.EmojiProvider
import com.chronoio.chrono.emojifier.suggest.EmojiSuggester
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.common.Action
import com.chronoio.chrono.presentation.common.Delegator
import com.chronoio.chrono.presentation.editor.cover.CoverImageHashProvider
import com.chronoio.chrono.presentation.navigation.backstack.BackHistoryDelegate
import com.chronoio.chrono.presentation.sets.state.ObjectStateReducer
import com.chronoio.chrono.presentation.vault.ExitToVaultDelegate
import com.chronoio.chrono.presentation.sets.subscription.DataViewSubscription
import com.chronoio.chrono.presentation.sets.viewer.ViewerDelegate
import com.chronoio.chrono.presentation.templates.ObjectTypeTemplatesContainer
import com.chronoio.chrono.presentation.util.Dispatcher

class ObjectSetViewModelFactory(
    private val params: ObjectSetViewModel.Params,
    private val permissions: UserPermissionProvider,
    private val openObjectSet: OpenObjectSet,
    private val closeObject: CloseObject,
    private val setObjectDetails: UpdateDetail,
    private val createDataViewObject: CreateDataViewObject,
    private val downloadUnsplashImage: DownloadUnsplashImage,
    private val setDocCoverImage: SetDocCoverImage,
    private val updateText: UpdateText,
    private val createBlock: CreateBlock,
    private val interceptEvents: InterceptEvents,
    private val dispatcher: Dispatcher<Payload>,
    private val delegator: Delegator<Action>,
    private val coverImageHashProvider: CoverImageHashProvider,
    private val urlBuilder: UrlBuilder,
    private val session: ObjectSetSession,
    private val analytics: Analytics,
    private val createObject: CreateObject,
    private val dataViewSubscriptionContainer: DataViewSubscriptionContainer,
    private val setQueryToObjectSet: SetQueryToObjectSet,
    private val database: ObjectSetDatabase,
    private val paginator: ObjectSetPaginator,
    private val storeOfRelations: StoreOfRelations,
    private val objectStateReducer: ObjectStateReducer,
    private val dataViewSubscription: DataViewSubscription,
    private val objectStore: ObjectStore,
    private val addObjectToCollection: AddObjectToCollection,
    private val objectToCollection: ConvertObjectToCollection,
    private val removeObjectFromCollection: RemoveObjectFromCollection,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val duplicateObjects: DuplicateObjects,
    private val templatesContainer: ObjectTypeTemplatesContainer,
    private val setObjectListIsArchived: SetObjectListIsArchived,
    private val createTemplate: CreateTemplate,
    private val viewerDelegate: ViewerDelegate,
    private val spaceManager: SpaceManager,
    private val dateProvider: DateProvider,
    private val spaceSyncAndP2PStatusProvider: SpaceSyncAndP2PStatusProvider,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val fieldParser: FieldParser,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val deepLinkResolver: DeepLinkResolver,
    private val setDataViewProperties: SetDataViewProperties,
    private val setDataViewObjectOrder: SetDataViewObjectOrder,
    private val boardGroupSubscriptionContainer: BoardGroupSubscriptionContainer,
    private val boardRecordsSubscriptionContainer: BoardRecordsSubscriptionContainer,
    private val storelessSubscriptionContainer: StorelessSubscriptionContainer,
    private val emojiProvider: EmojiProvider,
    private val emojiSuggester: EmojiSuggester,
    private val stringResourceProvider: StringResourceProvider,
    private val getDefaultObjectType: GetDefaultObjectType,
    private val addDiscussion: AddDiscussion,
    private val userSettingsRepository: UserSettingsRepository,
    private val backHistoryDelegate: BackHistoryDelegate,
    private val exitToVaultDelegate: ExitToVaultDelegate
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ObjectSetViewModel(
            vmParams = params,
            permissions = permissions,
            openObjectSet = openObjectSet,
            closeObject = closeObject,
            setObjectDetails = setObjectDetails,
            createDataViewObject = createDataViewObject,
            setDocCoverImage = setDocCoverImage,
            downloadUnsplashImage = downloadUnsplashImage,
            updateText = updateText,
            interceptEvents = interceptEvents,
            dispatcher = dispatcher,
            delegator = delegator,
            coverImageHashProvider = coverImageHashProvider,
            urlBuilder = urlBuilder,
            session = session,
            analytics = analytics,
            createObject = createObject,
            dataViewSubscriptionContainer = dataViewSubscriptionContainer,
            setQueryToObjectSet = setQueryToObjectSet,
            database = database,
            paginator = paginator,
            storeOfRelations = storeOfRelations,
            stateReducer = objectStateReducer,
            dataViewSubscription = dataViewSubscription,
            objectStore = objectStore,
            addObjectToCollection = addObjectToCollection,
            objectToCollection = objectToCollection,
            storeOfObjectTypes = storeOfObjectTypes,
            duplicateObjects = duplicateObjects,
            templatesContainer = templatesContainer,
            setObjectListIsArchived = setObjectListIsArchived,
            viewerDelegate = viewerDelegate,
            spaceManager = spaceManager,
            createTemplate = createTemplate,
            dateProvider = dateProvider,
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
            createBlock = createBlock,
            stringResourceProvider = stringResourceProvider,
            getDefaultObjectType = getDefaultObjectType,
            addDiscussion = addDiscussion,
            userSettingsRepository = userSettingsRepository,
            backHistoryDelegate = backHistoryDelegate,
            exitToVaultDelegate = exitToVaultDelegate
        ) as T
    }
}