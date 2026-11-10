package com.chronoio.chrono.presentation.widgets

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_utils.tools.toPrettyString
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber

interface WidgetActiveViewStateHolder {

    fun init(map: WidgetToActiveView)
    fun getActiveViews(): WidgetToActiveView
    fun onChangeCurrentWidgetView(widget: Id, view: Id)
    fun observeCurrentWidgetView(widget: Id): Flow<Id?>

    class Impl @Inject constructor() : WidgetActiveViewStateHolder {

        private val widgetToActiveView = MutableStateFlow<WidgetToActiveView>(mapOf())

        init {
            Timber.d("WidgetActiveViewStateHolder initialized")
        }

        override fun init(map: WidgetToActiveView) {
            Timber.d("Initializing active view: ${map.toPrettyString()}")
            widgetToActiveView.value = map
        }

        override fun getActiveViews(): WidgetToActiveView {
            return widgetToActiveView.value
        }

        override fun onChangeCurrentWidgetView(widget: Id, view: Id) {
            widgetToActiveView.value = widgetToActiveView.value + mapOf(widget to view)
        }

        override fun observeCurrentWidgetView(widget: Id) = widgetToActiveView.mapLatest { map ->
            map[widget]
        }
    }
}

typealias WidgetToActiveView = Map<Id, Id>
typealias WidgetActiveViewId = Id