package com.chronoio.chrono.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Event
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.tools.FeatureToggles
import com.chronoio.chrono.domain.ai.TypeSuggestionEngine
import com.chronoio.chrono.domain.auth.interactor.ClearLastOpenedObject
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.interactor.RemoveLinkMark
import com.chronoio.chrono.domain.block.interactor.SetObjectType
import com.chronoio.chrono.domain.block.interactor.UpdateLinkMarks
import com.chronoio.chrono.domain.block.interactor.sets.CreateObjectSet
import com.chronoio.chrono.domain.block.interactor.sets.GetObjectTypes
import com.chronoio.chrono.domain.cover.SetDocCoverImage
import com.chronoio.chrono.domain.chats.GetChatMessages
import com.chronoio.chrono.domain.discussions.AddDiscussion
import com.chronoio.chrono.domain.event.interactor.InterceptEvents
import com.chronoio.chrono.domain.event.interactor.SpaceSyncAndP2PStatusProvider
import com.chronoio.chrono.domain.icon.SetDocumentImageIcon
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.networkmode.GetNetworkMode
import com.chronoio.chrono.domain.`object`.ConvertObjectToCollection
import com.chronoio.chrono.domain.`object`.ConvertObjectToSet
import com.chronoio.chrono.domain.`object`.UpdateDetail
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.page.CloseObject
import com.chronoio.chrono.domain.page.CreateBlockLinkWithObject
import com.chronoio.chrono.domain.page.CreateObject
import com.chronoio.chrono.domain.page.CreateObjectAsMentionOrLink
import com.chronoio.chrono.domain.page.OpenPage
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.relations.AddRelationToObject
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.sets.FindObjectSetForType
import com.chronoio.chrono.domain.templates.ApplyTemplate
import com.chronoio.chrono.domain.unsplash.DownloadUnsplashImage
import com.chronoio.chrono.domain.workspace.InterceptFileLimitEvents
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.common.Action
import com.chronoio.chrono.presentation.common.Delegator
import com.chronoio.chrono.presentation.common.StateReducer
import com.chronoio.chrono.presentation.editor.editor.Orchestrator
import com.chronoio.chrono.presentation.editor.editor.table.EditorTableDelegate
import com.chronoio.chrono.presentation.editor.render.DefaultBlockViewRenderer
import com.chronoio.chrono.presentation.navigation.backstack.BackHistoryDelegate
import com.chronoio.chrono.presentation.vault.ExitToVaultDelegate
import com.chronoio.chrono.presentation.templates.ObjectTypeTemplatesContainer
import com.chronoio.chrono.presentation.util.CopyFileToCacheDirectory
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.util.UrlHelper
import javax.inject.Inject

open class EditorViewModelFactory @Inject constructor(
    private val params: EditorViewModel.Params,
    private val permissions: UserPermissionProvider,
    private val openPage: OpenPage,
    private val closeObject: CloseObject,
    private val createObjectSet: CreateObjectSet,
    private val createBlockLinkWithObject: CreateBlockLinkWithObject,
    private val createObjectAsMentionOrLink: CreateObjectAsMentionOrLink,
    private val interceptEvents: InterceptEvents,
    private val updateLinkMarks: UpdateLinkMarks,
    private val removeLinkMark: RemoveLinkMark,
    private val documentEventReducer: StateReducer<List<Block>, Event>,
    private val urlBuilder: UrlBuilder,
    private val renderer: DefaultBlockViewRenderer,
    private val orchestrator: Orchestrator,
    private val analytics: Analytics,
    private val dispatcher: Dispatcher<Payload>,
    private val delegator: Delegator<Action>,
    private val updateDetail: UpdateDetail,
    private val searchObjects: SearchObjects,
    private val getDefaultObjectType: GetDefaultObjectType,
    private val findObjectSetForType: FindObjectSetForType,
    private val copyFileToCacheDirectory: CopyFileToCacheDirectory,
    private val downloadUnsplashImage: DownloadUnsplashImage,
    private val setDocCoverImage: SetDocCoverImage,
    private val setDocImageIcon: SetDocumentImageIcon,
    private val createObject: CreateObject,
    private val objectToSet: ConvertObjectToSet,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val featureToggles: FeatureToggles,
    private val tableDelegate: EditorTableDelegate,
    private val spaceManager: SpaceManager,
    private val getObjectTypes: GetObjectTypes,
    private val objectToCollection: ConvertObjectToCollection,
    private val interceptFileLimitEvents: InterceptFileLimitEvents,
    private val addRelationToObject: AddRelationToObject,
    private val applyTemplate: ApplyTemplate,
    private val setObjectType: SetObjectType,
    private val templatesContainer: ObjectTypeTemplatesContainer,
    private val storelessSubscriptionContainer: StorelessSubscriptionContainer,
    private val dispatchers: AppCoroutineDispatchers,
    private val getNetworkMode: GetNetworkMode,
    private val clearLastOpenedObject: ClearLastOpenedObject,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val syncStatusProvider: SpaceSyncAndP2PStatusProvider,
    private val fieldParser : FieldParser,
    private val dateProvider: DateProvider,
    private val spaceViews: SpaceViewSubscriptionContainer,
    private val urlHelper: UrlHelper,
    private val addDiscussion: AddDiscussion,
    private val getChatMessages: GetChatMessages,
    private val backHistoryDelegate: BackHistoryDelegate,
    private val exitToVaultDelegate: ExitToVaultDelegate,
    private val typeSuggestionEngine: TypeSuggestionEngine = TypeSuggestionEngine.NoOp
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditorViewModel(
            vmParams = params,
            permissions = permissions,
            openPage = openPage,
            closePage = closeObject,
            createBlockLinkWithObject = createBlockLinkWithObject,
            createObjectAsMentionOrLink = createObjectAsMentionOrLink,
            interceptEvents = interceptEvents,
            updateLinkMarks = updateLinkMarks,
            removeLinkMark = removeLinkMark,
            reducer = documentEventReducer,
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
            copyFileToCache = copyFileToCacheDirectory,
            downloadUnsplashImage = downloadUnsplashImage,
            setDocCoverImage = setDocCoverImage,
            setDocImageIcon = setDocImageIcon,
            createObject = createObject,
            objectToSet = objectToSet,
            objectToCollection = objectToCollection,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            featureToggles = featureToggles,
            tableDelegate = tableDelegate,
            spaceManager = spaceManager,
            getObjectTypes = getObjectTypes,
            interceptFileLimitEvents = interceptFileLimitEvents,
            addRelationToObject = addRelationToObject,
            applyTemplate = applyTemplate,
            setObjectType = setObjectType,
            templatesContainer = templatesContainer,
            dispatchers = dispatchers,
            storelessSubscriptionContainer = storelessSubscriptionContainer,
            spaceSyncAndP2PStatusProvider = syncStatusProvider,
            getNetworkMode = getNetworkMode,
            analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
            clearLastOpenedObject = clearLastOpenedObject,
            fieldParser = fieldParser,
            dateProvider = dateProvider,
            spaceViews = spaceViews,
            urlHelper = urlHelper,
            addDiscussion = addDiscussion,
            getChatMessages = getChatMessages,
            backHistoryDelegate = backHistoryDelegate,
            exitToVaultDelegate = exitToVaultDelegate,
            typeSuggestionEngine = typeSuggestionEngine
        ) as T
    }
}
