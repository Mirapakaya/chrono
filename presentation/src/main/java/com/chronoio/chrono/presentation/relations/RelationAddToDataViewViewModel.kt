package com.chronoio.chrono.presentation.relations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.analytics.base.EventsDictionary
import com.chronoio.chrono.core_models.DVViewerRelation
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Key
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.fold
import com.chronoio.chrono.domain.dataview.interactor.AddRelationToDataView
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.relations.GetRelations
import com.chronoio.chrono.domain.workspace.AddObjectToWorkspace
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.extension.sendAnalyticsRelationEvent
import com.chronoio.chrono.presentation.relations.providers.ObjectRelationProvider
import com.chronoio.chrono.presentation.sets.dataViewState
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.sets.viewerById
import com.chronoio.chrono.presentation.util.Dispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RelationAddToDataViewViewModel(
    relationsProvider: ObjectRelationProvider,
    val vmParams: VmParams,
    private val objectState: StateFlow<ObjectState>,
    private val updateDataViewViewer: UpdateDataViewViewer,
    private val addRelationToDataView: AddRelationToDataView,
    private val getRelations: GetRelations,
    private val dispatcher: Dispatcher<Payload>,
    private val analytics: Analytics,
    private val addObjectToWorkspace: AddObjectToWorkspace,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val storeOfRelations: StoreOfRelations
) : RelationAddViewModelBase(
    vmParams = vmParams,
    relationsProvider = relationsProvider,
    appCoroutineDispatchers = appCoroutineDispatchers,
    getRelations = getRelations,
    addObjectToWorkspace = addObjectToWorkspace,
) {

    fun onRelationSelected(
        ctx: Id,
        viewerId: Id,
        relation: Key,
        dv: Id,
        screenType: String
    ) {
        viewModelScope.launch {
            addRelationToDataView(
                AddRelationToDataView.Params(
                    ctx = ctx,
                    relation = relation,
                    dv = dv
                )
            ).process(
                success = {
                    dispatcher.send(it).also {
                        proceedWithAddingNewRelationToCurrentViewer(
                            ctx = ctx,
                            viewerId = viewerId,
                            relation = relation
                        )
                    }
                    analytics.sendAnalyticsRelationEvent(
                        eventName = EventsDictionary.relationAdd,
                        storeOfRelations = storeOfRelations,
                        relationKey = relation,
                        type = screenType,
                        spaceParams = analyticSpaceHelperDelegate.provideParams(vmParams.space.id)
                    )
                },
                failure = {
                    Timber.e(it, ERROR_MESSAGE)
                    _toasts.emit("$ERROR_MESSAGE: ${it.localizedMessage}")
                }
            )
        }
    }
    
    private suspend fun proceedWithAddingNewRelationToCurrentViewer(ctx: Id, viewerId: Id, relation: Id) {
        val state = objectState.value.dataViewState() ?: return
        val viewer = state.viewerById(viewerId) ?: return

        updateDataViewViewer.async(
            UpdateDataViewViewer.Params.ViewerRelation.Add(
                ctx = ctx,
                dv = state.dataViewBlock.id,
                view = viewer.id,
                relation = DVViewerRelation(
                    key = relation,
                    isVisible = true
                )
            )
        ).fold(
            onSuccess = { dispatcher.send(it).also { isDismissed.value = true } },
            onFailure = { Timber.e(it, "Error while updating data view's viewer") }
        )
    }

    class Factory(
        private val vmParams: VmParams,
        private val state: StateFlow<ObjectState>,
        private val updateDataViewViewer: UpdateDataViewViewer,
        private val addRelationToDataView: AddRelationToDataView,
        private val dispatcher: Dispatcher<Payload>,
        private val analytics: Analytics,
        private val relationsProvider: ObjectRelationProvider,
        private val appCoroutineDispatchers: AppCoroutineDispatchers,
        private val getRelations: GetRelations,
        private val addObjectToWorkspace: AddObjectToWorkspace,
        private val analyticSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
        private val storeOfRelations: StoreOfRelations
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RelationAddToDataViewViewModel(
                vmParams = vmParams,
                addRelationToDataView = addRelationToDataView,
                dispatcher = dispatcher,
                updateDataViewViewer = updateDataViewViewer,
                objectState = state,
                analytics = analytics,
                relationsProvider = relationsProvider,
                appCoroutineDispatchers = appCoroutineDispatchers,
                getRelations = getRelations,
                addObjectToWorkspace = addObjectToWorkspace,
                analyticSpaceHelperDelegate = analyticSpaceHelperDelegate,
                storeOfRelations = storeOfRelations
            ) as T
        }
    }
}