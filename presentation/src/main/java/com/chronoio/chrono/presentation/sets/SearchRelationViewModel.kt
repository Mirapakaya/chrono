package com.chronoio.chrono.presentation.sets

import androidx.lifecycle.viewModelScope
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.RelationFormat
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_utils.ext.cancel
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.presentation.common.BaseListViewModel
import com.chronoio.chrono.presentation.relations.simpleRelations
import com.chronoio.chrono.presentation.sets.model.SimpleRelationView
import com.chronoio.chrono.presentation.sets.state.ObjectState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Inherit this class in order to enable search-for-relations feature.
 */
abstract class SearchRelationViewModel(
    private val objectState: StateFlow<ObjectState>,
    private val storeOfRelations: StoreOfRelations
) : BaseListViewModel<SimpleRelationView>() {

    val isDismissed = MutableSharedFlow<Boolean>(replay = 0)

    private val query = Channel<String>()
    private val jobs = mutableListOf<Job>()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onStart(viewerId: Id) {
        Timber.d("SearchRelationViewModel, onStart, viewerId: [$viewerId]")
        // Initializing views before any query.
        jobs += viewModelScope.launch {
            val initViews = getPropertiesForDataView()
            Timber.d("SearchRelationViewModel, initRelationViews: [$initViews]")
            _views.value = initViews
        }
        // Searching and mapping views based on query changes.
        jobs += viewModelScope.launch {
            query
                .consumeAsFlow()
                .mapLatest { queryText ->
                    val relations = getPropertiesForDataView()
                    if (queryText.isEmpty()) {
                        relations
                    } else {
                        relations.filter { it.title.contains(queryText, ignoreCase = true) }
                    }
                }
                .collect { _views.value = it }
        }
    }

    /**
     * Fetches properties from the DataView's relationLinks and maps them to [SimpleRelationView].
     * After mapping, filters out relations that are:
     * - Hidden (isHidden = true), except for Name and Done which are always shown
     * - Of disallowed formats (RELATIONS, EMOJI, UNDEFINED)
     *
     * @see notAllowedRelations for filtering logic
     */
    private suspend fun getPropertiesForDataView(): List<SimpleRelationView> {
        val dv = objectState.value.dataViewState()?.dataViewContent ?: return emptyList()
        val dvRelations = dv.relationLinks.mapNotNull { storeOfRelations.getByKey(it.key) }
        return dvRelations.map { property ->
            SimpleRelationView(
                key = property.key,
                title = property.name.orEmpty(),
                format = property.format,
                isHidden = property.isHidden ?: false,
                isReadonly = property.isReadonlyValue,
                isDefault = Relations.systemRelationKeys.contains(property.key)
            )
        }.filterNot { notAllowedRelations(it) }
    }

    fun onStop() {
        jobs.cancel()
    }

    protected open suspend fun filterRelationsFromAlreadyInUse(
        objectState: ObjectState,
        viewerId: String?,
        storeOfRelations: StoreOfRelations
    ): List<SimpleRelationView> {
        return objectState.simpleRelations(
            viewerId = viewerId,
            storeOfRelations = storeOfRelations
        )
    }

    private fun notAllowedRelations(relation: SimpleRelationView): Boolean =
        notAllowedRelationFormats.contains(relation.format)
                || (relation.key != Relations.NAME && relation.key != Relations.DONE && relation.isHidden)

    fun onSearchQueryChanged(txt: String) {
        viewModelScope.launch { query.send(txt) }
    }

    companion object {
        val notAllowedRelationFormats = listOf(
            RelationFormat.RELATIONS,
            RelationFormat.EMOJI,
            RelationFormat.UNDEFINED
        )
    }
}