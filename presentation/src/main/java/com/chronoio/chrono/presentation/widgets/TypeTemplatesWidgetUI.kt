package com.chronoio.chrono.presentation.widgets

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.presentation.templates.TemplateObjectTypeView
import com.chronoio.chrono.presentation.templates.TemplateView

sealed class TypeTemplatesWidgetUI {
    abstract val showWidget: Boolean
    abstract val isEditing: Boolean
    abstract val isPossibleToChangeTemplate: Boolean

    data class Init(
        override val showWidget: Boolean = false,
        override val isEditing: Boolean = false,
        override val isPossibleToChangeTemplate: Boolean = false
    ) : TypeTemplatesWidgetUI()

    data class Data(
        override val showWidget: Boolean,
        override val isEditing: Boolean,
        val moreMenuItem: TemplateView? = null,
        val templates: List<TemplateView> = emptyList(),
        val objectTypes: List<TemplateObjectTypeView> = emptyList(),
        val viewerId: Id,
        val isPossibleToChangeType: Boolean,
        override val isPossibleToChangeTemplate: Boolean
    ) : TypeTemplatesWidgetUI()

    fun getWidgetViewerId(): Id? = if (this is Data) viewerId else null
}

sealed class TypeTemplatesWidgetUIAction {

    sealed class TypeClick : TypeTemplatesWidgetUIAction() {
        data class Item(val type: ObjectWrapper.Type) : TypeClick()
        object Search : TypeClick()
    }

    data class TemplateClick(val template: TemplateView): TypeTemplatesWidgetUIAction()
}

fun TypeTemplatesWidgetUI.Data.enterEditing(): TypeTemplatesWidgetUI.Data {
    return this.copy(isEditing = true)
}

fun TypeTemplatesWidgetUI.Data.exitEditing(): TypeTemplatesWidgetUI.Data {
    return this.copy(isEditing = false, moreMenuItem = null)
}

fun TypeTemplatesWidgetUI.Data.showMoreMenu(item: TemplateView): TypeTemplatesWidgetUI.Data {
    return this.copy(moreMenuItem = item)
}

fun TypeTemplatesWidgetUI.Data.hideMoreMenu(): TypeTemplatesWidgetUI.Data {
    return this.copy(moreMenuItem = null)
}
