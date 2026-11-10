package com.chronoio.chrono.presentation.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.DVSort
import com.chronoio.chrono.core_models.DVSortType
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.fold
import com.chronoio.chrono.domain.dataview.interactor.UpdateDataViewViewer
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.presentation.extension.ObjectStateAnalyticsEvent
import com.chronoio.chrono.presentation.extension.logEvent
import com.chronoio.chrono.presentation.relations.simpleRelations
import com.chronoio.chrono.presentation.sets.model.SimpleRelationView
import com.chronoio.chrono.presentation.sets.state.ObjectState
import com.chronoio.chrono.presentation.util.Dispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectSortRelationViewModel(
    private val objectState: StateFlow<ObjectState>,
    private val dispatcher: Dispatcher<Payload>,
    private val updateDataViewViewer: UpdateDataViewViewer,
    private val storeOfRelations: StoreOfRelations,
    private val analytics: Analytics
) : SearchRelationViewModel(
    objectState = objectState,
    storeOfRelations = storeOfRelations
) {

    fun onRelationClicked(ctx: Id, viewerId: Id, relation: SimpleRelationView) {
        val state = objectState.value.dataViewState() ?: return
        val viewer = state.viewerById(viewerId) ?: return
        val startTime = System.currentTimeMillis()
        viewModelScope.launch {
            val params = UpdateDataViewViewer.Params.Sort.Add(
                ctx = ctx,
                dv = state.dataViewBlock.id,
                view = viewer.id,
                sort = DVSort(
                    relationKey = relation.key,
                    type = DVSortType.ASC,
                    relationFormat = relation.format
                )
            )
            updateDataViewViewer.async(params).fold(
                onSuccess = {
                    dispatcher.send(it).also {
                        logEvent(
                            state = objectState.value,
                            analytics = analytics,
                            event = ObjectStateAnalyticsEvent.ADD_SORT,
                            startTime = startTime,
                            type = DVSortType.ASC.formattedName
                        )
                        isDismissed.emit(true)
                    }
                },
                onFailure = {
                    Timber.e(it, "Error while adding a sort").also {
                        _toasts.emit(USE_CASE_ERROR)
                    }
                }
            )
        }
    }

    class Factory(
        private val objectState: StateFlow<ObjectState>,
        private val dispatcher: Dispatcher<Payload>,
        private val updateDataViewViewer: UpdateDataViewViewer,
        private val storeOfRelations: StoreOfRelations,
        private val analytics: Analytics
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SelectSortRelationViewModel(
                objectState = objectState,
                dispatcher = dispatcher,
                updateDataViewViewer = updateDataViewViewer,
                storeOfRelations = storeOfRelations,
                analytics = analytics
            ) as T
        }
    }

    companion object {
        const val USE_CASE_ERROR = "Couldn't add a sort. Please, try again."
    }

    override suspend fun filterRelationsFromAlreadyInUse(
        objectState: ObjectState,
        viewerId: String?,
        storeOfRelations: StoreOfRelations
    ): List<SimpleRelationView> {
        val dataViewState = objectState.dataViewState() ?: return emptyList()
        val relationsInUse: List<String> = run {
            val dv = dataViewState.dataViewContent
            val viewer = dv.viewers.find { it.id == viewerId } ?: dv.viewers.first()
            viewer.sorts.map { it.relationKey }.toList()
        }
        return objectState.simpleRelations(
            viewerId = viewerId,
            storeOfRelations = storeOfRelations
        ).filterNot { it.key in relationsInUse }
    }
}