package com.chronoio.chrono.presentation.sets.state

import com.chronoio.chrono.core_models.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

interface ObjectStateReducer {

    val state: MutableStateFlow<ObjectState>
    val effects: SharedFlow<List<StateSideEffect>>

    suspend fun run()
    suspend fun dispatch(events: List<Event>)
    fun reduce(state: ObjectState, events: List<Event>): DefaultObjectStateReducer.Transformation
    fun clear()
}