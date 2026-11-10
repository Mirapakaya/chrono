package com.chronoio.chrono.presentation.sets

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.domain.objects.ObjectStore
import com.chronoio.chrono.domain.search.DataViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class ObjectSetDatabase(
    val store: ObjectStore
) {
    private val _index: MutableStateFlow<DataViewState> = MutableStateFlow(DataViewState.Init)
    val index: StateFlow<DataViewState> = _index

    fun update(update: DataViewState) {
        _index.value = update
    }

    fun observe(target: Id) : Flow<ObjectWrapper.Basic> = index
        .onEach { Timber.d("SET-DB: Index changed") }
        .mapNotNull { store.get(target) }
}