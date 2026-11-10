package com.chronoio.chrono.presentation.widgets

import com.chronoio.chrono.presentation.home.Command.ChangeWidgetType.Companion.TYPE_COMPACT_LIST
import com.chronoio.chrono.presentation.home.Command.ChangeWidgetType.Companion.TYPE_LINK
import com.chronoio.chrono.presentation.home.Command.ChangeWidgetType.Companion.TYPE_LIST
import com.chronoio.chrono.presentation.home.Command.ChangeWidgetType.Companion.TYPE_TREE
import com.chronoio.chrono.presentation.home.Command.ChangeWidgetType.Companion.TYPE_VIEW

sealed class WidgetTypeView {
    abstract val isSelected: Boolean

    data class List(override val isSelected: Boolean = false) : WidgetTypeView()
    data class CompactList(override val isSelected: Boolean = false) : WidgetTypeView()
    data class Tree(override val isSelected: Boolean = false) : WidgetTypeView()
    data class Link(override val isSelected: Boolean = false) : WidgetTypeView()
    data class View(override val isSelected: Boolean = false) : WidgetTypeView()

    fun setIsSelected(type: Int): WidgetTypeView = when (this) {
        is Link -> copy(isSelected = type == TYPE_LINK)
        is List -> copy(isSelected = type == TYPE_LIST)
        is CompactList -> copy(isSelected = type == TYPE_COMPACT_LIST)
        is Tree -> copy(isSelected = type == TYPE_TREE)
        is View -> copy(isSelected = type == TYPE_VIEW)
    }
}