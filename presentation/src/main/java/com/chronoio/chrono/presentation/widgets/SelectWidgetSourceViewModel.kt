package com.chronoio.chrono.presentation.widgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.widgets.BundledWidgetSourceIds
import com.chronoio.chrono.domain.base.Resultat
import com.chronoio.chrono.domain.base.fold
import com.chronoio.chrono.domain.base.onSuccess
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.domain.widgets.GetSuggestedWidgetTypes
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.extension.sendChangeWidgetSourceEvent
import com.chronoio.chrono.core_models.ui.objectIcon
import com.chronoio.chrono.presentation.navigation.DefaultObjectView
import com.chronoio.chrono.presentation.search.ObjectSearchConstants
import com.chronoio.chrono.presentation.search.ObjectSearchSection
import com.chronoio.chrono.presentation.search.ObjectSearchView
import com.chronoio.chrono.presentation.search.ObjectSearchViewModel
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.presentation.widgets.source.BundledWidgetSourceView
import com.chronoio.chrono.presentation.widgets.source.SuggestWidgetObjectType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectWidgetSourceViewModel(
    private val vmParams: VmParams,
    private val urlBuilder: UrlBuilder,
    private val searchObjects: SearchObjects,
    private val analytics: Analytics,
    private val dispatcher: Dispatcher<WidgetDispatchEvent>,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val getSuggestedWidgetTypes: GetSuggestedWidgetTypes,
    private val fieldParser: FieldParser,
    private val spaceViews: SpaceViewSubscriptionContainer
) : ObjectSearchViewModel(
    vmParams = vmParams,
    urlBuilder = urlBuilder,
    searchObjects = searchObjects,
    analytics = analytics,
    analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
    fieldParser = fieldParser,
    storeOfObjectTypes = storeOfObjectTypes,
    spaceViews = spaceViews
) {

    private val suggested = MutableStateFlow<SuggestedWidgetsState>(SuggestedWidgetsState.Init)

    val isDismissed = MutableStateFlow(false)
    var config : Config = Config.None

    val viewState = flow {
        emitAll(
            combine(
                stateData.asFlow(),
                suggested.filterIsInstance<SuggestedWidgetsState.Default>(),
                spaceViews.observe(vmParams.space)
            ) { state, suggested, spaceView ->
                val hasChat = !spaceView.chatId.isNullOrEmpty()

                when(state) {
                    is ObjectSearchView.Success -> {
                        state.copy(
                            objects = buildList {
                                val query = userInput.value
                                addAll(
                                    resolveSuggestedResults(
                                        suggested = suggested,
                                        query = query,
                                        hasChat = hasChat
                                    )
                                )
                                // Widgets from existing objects
                                add(ObjectSearchSection.SelectWidgetSource.FromMyObjects)
                                addAll(state.objects)
                            }
                        )
                    }
                    is ObjectSearchView.NoResults -> {
                        val query = state.searchText
                        val result = buildList {
                            addAll(
                                resolveSuggestedResults(
                                    suggested = suggested,
                                    query = query,
                                    hasChat = hasChat
                                )
                            )
                        }
                        if (result.isNotEmpty()) {
                            ObjectSearchView.Success(result)
                        } else {
                            state
                        }
                    }
                    else -> state
                }
            }
        )
    }

    init {
        viewModelScope.launch {
            dispatcher.flow()
                .filterIsInstance<WidgetDispatchEvent.TypePicked>()
                .take(1)
                .collect {
                    isDismissed.value = true
                }
        }
    }

    private fun resolveSuggestedResults(
        suggested: SuggestedWidgetsState.Default,
        query: String,
        hasChat: Boolean
    ) = buildList {

        // Adding system widgets if matched by query

        val filteredSuggestedSystemSources = suggested.suggestedSystemSources.filter { source ->
            source.contains(query, ignoreCase = true)
        }
        if (filteredSuggestedSystemSources.isNotEmpty()) {
            add(ObjectSearchSection.SelectWidgetSource.System)
            with(filteredSuggestedSystemSources) {
                if (contains(BundledWidgetSourceIds.FAVORITE)) {
                    add(BundledWidgetSourceView.Favorites)
                }
                if (contains(BundledWidgetSourceIds.ALL_OBJECTS)) {
                    add(BundledWidgetSourceView.AllObjects)
                }
                // Chat widget can only be added if the space has chat.
                if (contains(BundledWidgetSourceIds.CHAT) && hasChat) {
                    add(BundledWidgetSourceView.Chat)
                }
                if (contains(BundledWidgetSourceIds.RECENT)) {
                    add(BundledWidgetSourceView.Recent)
                }
                if (contains(BundledWidgetSourceIds.RECENT_LOCAL)) {
                    add(BundledWidgetSourceView.RecentLocal)
                }
                if (contains(BundledWidgetSourceIds.BIN)) {
                    add(BundledWidgetSourceView.Bin)
                }
            }
        }

        // Adding object type widgets (aka object type widgets) if matched by query

        val filteredSuggestedObjectTypes = suggested.suggestedObjectTypes.filter { type ->
            type.name.contains(query, ignoreCase = true)
        }

        if (filteredSuggestedObjectTypes.isNotEmpty()) {
            add(ObjectSearchSection.SelectWidgetSource.Suggested)
            addAll(filteredSuggestedObjectTypes)
        }
    }

    override fun resolveViews(result: Resultat<List<DefaultObjectView>>) {
        viewModelScope.launch {
            result.fold(
                onSuccess = { views ->
                    if (views.isEmpty()) {
                        stateData.postValue(ObjectSearchView.NoResults(userInput.value))
                    } else {
                        stateData.postValue(ObjectSearchView.Success(views))
                    }
                },
                onLoading = {
                    stateData.postValue(ObjectSearchView.Loading)
                },
                onFailure = {
                    Timber.e(it, "Error while selecting source for widget")
                }
            )
        }
    }

    fun onStartWithNewWidget(
        ctx: Id,
        target: Id?,
        isInEditMode: Boolean
    ) {
        Timber.d("onStart with picking source for new widget")
        config = Config.NewWidget(
            ctx = ctx,
            target = target,
            isInEditMode = isInEditMode
        )
        proceedWithSearchQuery(ctx)
    }

    fun onStartWithExistingWidget(
        ctx: Id,
        widget: Id,
        source: Id,
        type: Int,
        isInEditMode: Boolean
    ) {
        Timber.d("onStart with picking source for an existing widget")
        config = Config.ExistingWidget(
            ctx = ctx,
            widget = widget,
            source = source,
            type = type,
            isInEditMode = isInEditMode
        )
        proceedWithSearchQuery(ctx)
    }

    private fun proceedWithSearchQuery(ctx: Id) {
        viewModelScope.launch {
            getSuggestedWidgetTypes.async(
                params = GetSuggestedWidgetTypes.Params(
                    space = vmParams.space,
                    objectTypeFilters = buildList {
                        add(
                            DVFilter(
                                relation = Relations.SPACE_ID,
                                condition = DVFilterCondition.EQUAL,
                                value = vmParams.space.id
                            )
                        )
                        addAll(ObjectSearchConstants.filterTypes())
                    },
                    objectTypeKeys = ObjectSearchConstants.defaultKeysObjectType,
                    ctx = ctx
                )
            ).onSuccess { result ->
                suggested.value = SuggestedWidgetsState.Default(
                    suggestedSystemSources = result.suggestedSystemSources
                        .filterNot { it == BundledWidgetSourceIds.CHAT },
                    suggestedObjectTypes = result.suggestedObjectTypes.map { type ->
                        SuggestWidgetObjectType(
                            id = type.id,
                            name = fieldParser.getObjectPluralName(type),
                            objectIcon = type.objectIcon()
                        )
                    }
                )
            }
        }
        startProcessingSearchQuery(null)
    }

    override suspend fun getSearchObjectsParams(ignore: Id?): SearchObjects.Params {
        val isOneToOneSpace = spaceViews.get(vmParams.space)?.isOneToOneSpace == true
        return super.getSearchObjectsParams(ignore).copy(
            filters = ObjectSearchConstants.filterSearchObjects(
                excludeTypes = true,
                isOneToOneSpace = isOneToOneSpace
            )
        )
    }

    fun onBundledWidgetSourceClicked(view: BundledWidgetSourceView) {
        Timber.d("onBundledWidgetSourceClicked, view:[$view]")
        when (val curr = config) {
            is Config.NewWidget -> {
                viewModelScope.launch {
                    dispatcher.send(
                        WidgetDispatchEvent.SourcePicked.Bundled(
                            source = view.id,
                            target = curr.target
                        )
                    ).also {
                        sendChangeWidgetSourceEvent(
                            analytics = analytics,
                            view = view,
                            isForNewWidget = true,
                            isInEditMode = curr.isInEditMode
                        )
                    }
                    if (
                        view is BundledWidgetSourceView.AllObjects
                        || view is BundledWidgetSourceView.Bin
                        || view is BundledWidgetSourceView.Chat
                    ) {
                        isDismissed.value = true
                    }
                }
            }
            is Config.ExistingWidget -> {
                viewModelScope.launch {
                    dispatcher.send(
                        WidgetDispatchEvent.SourceChanged(
                            ctx = curr.ctx,
                            widget = curr.widget,
                            source = view.id,
                            type = curr.type
                        )
                    ).also {
                        sendChangeWidgetSourceEvent(
                            analytics = analytics,
                            view = view,
                            isForNewWidget = false,
                            isInEditMode = curr.isInEditMode
                        )
                    }
                    isDismissed.value = true
                }
            }
            Config.None -> {
                Timber.w("Missing config for widget source")
            }
        }
    }

    fun onSuggestedWidgetObjectTypeClicked(view: SuggestWidgetObjectType) {
        Timber.d("onSuggestedWidgetObjectTypeClicked, view:[$view]")
        when (val curr = config) {
            is Config.NewWidget -> {
                viewModelScope.launch {
                    dispatcher.send(
                        WidgetDispatchEvent.SourcePicked.Default(
                            source = view.id,
                            target = curr.target,
                            sourceLayout = ObjectType.Layout.OBJECT_TYPE.code
                        )
                    ).also {
                        // TODO send analytics
                    }
                }
            }
            is Config.ExistingWidget -> {
                viewModelScope.launch {
                    dispatcher.send(
                        WidgetDispatchEvent.SourceChanged(
                            ctx = curr.ctx,
                            widget = curr.widget,
                            source = view.id,
                            type = curr.type
                        )
                    ).also {
                        // TODO send analytics
                    }
                    isDismissed.value = true
                }
            }
            Config.None -> {
                Timber.w("Missing config for widget source")
            }
        }
    }

    fun onCreateNewObjectClicked() {
        viewModelScope.launch {
            dispatcher.send(WidgetDispatchEvent.NewWithWidgetWithNewSource)
            isDismissed.value = true
        }
    }

    override fun onObjectClicked(view: DefaultObjectView) {
        Timber.d("onObjectClicked, view:[$view]")
        when(val curr = config) {
            is Config.NewWidget -> {
                viewModelScope.launch {
                    dispatcher.send(
                        WidgetDispatchEvent.SourcePicked.Default(
                            source = view.id,
                            sourceLayout = view.layout?.code ?: -1,
                            target = curr.target
                        )
                    ).also {
                        dispatchSelectCustomSourceAnalyticEvent(
                            view = view,
                            isForNewWidget = true,
                            isInEditMode = curr.isInEditMode
                        )
                    }
                    if (view.layout != null && WidgetConfig.isLinkOnlyLayout(view.layout.code)) {
                        isDismissed.value = true
                    }
                }
            }
            is Config.ExistingWidget -> {
                viewModelScope.launch {
                    dispatcher.send(
                        WidgetDispatchEvent.SourceChanged(
                            ctx = curr.ctx,
                            widget = curr.widget,
                            source = view.id,
                            type = curr.type
                        )
                    ).also {
                        dispatchSelectCustomSourceAnalyticEvent(
                            view = view,
                            isForNewWidget = false,
                            isInEditMode = curr.isInEditMode
                        )
                    }
                    isDismissed.value = true
                }
            }
            is Config.None -> {
                // Do nothing.
            }
        }
    }

    private suspend fun CoroutineScope.dispatchSelectCustomSourceAnalyticEvent(
        view: DefaultObjectView,
        isForNewWidget: Boolean,
        isInEditMode: Boolean
    ) {
        val sourceObjectType = storeOfObjectTypes.get(view.id)
        if (sourceObjectType != null) {
            sendChangeWidgetSourceEvent(
                analytics = analytics,
                sourceObjectTypeId = sourceObjectType.sourceObject.orEmpty(),
                isCustomObjectType = sourceObjectType.sourceObject.isNullOrEmpty(),
                isForNewWidget = isForNewWidget,
                isInEditMode = isInEditMode
            )
        } else {
            Timber.e("Could not found type for analytics")
        }
    }

    class Factory(
        private val vmParams: VmParams,
        private val urlBuilder: UrlBuilder,
        private val searchObjects: SearchObjects,
        private val analytics: Analytics,
        private val dispatcher: Dispatcher<WidgetDispatchEvent>,
        private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        private val fieldParser: FieldParser,
        private val storeOfObjectTypes: StoreOfObjectTypes,
        private val getSuggestedWidgetTypes: GetSuggestedWidgetTypes,
        private val spaceViews: SpaceViewSubscriptionContainer
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SelectWidgetSourceViewModel(
                vmParams = vmParams,
                urlBuilder = urlBuilder,
                searchObjects = searchObjects,
                analytics = analytics,
                dispatcher = dispatcher,
                analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
                fieldParser = fieldParser,
                storeOfObjectTypes = storeOfObjectTypes,
                getSuggestedWidgetTypes = getSuggestedWidgetTypes,
                spaceViews = spaceViews
            ) as T
        }
    }

    sealed class Config {
        data object None : Config()
        data class NewWidget(
            val ctx: Id,
            val target: Id?,
            val isInEditMode: Boolean
        ) : Config()
        data class ExistingWidget(
            val ctx: Id,
            val widget: Id,
            val source: Id,
            val type: Int,
            val isInEditMode: Boolean
        ) : Config()
    }

    sealed class SuggestedWidgetsState {
        data object Init : SuggestedWidgetsState()
        data class Default(
            val suggestedObjectTypes: List<SuggestWidgetObjectType>,
            val suggestedSystemSources: List<String>
        ) : SuggestedWidgetsState()
    }
}