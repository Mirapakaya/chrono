package com.chronoio.chrono.presentation.widgets.source

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.widgets.BundledWidgetSourceIds
import com.chronoio.chrono.presentation.navigation.DefaultSearchItem
import com.chronoio.chrono.core_models.ui.ObjectIcon

/**
 * Used for picking bundled widget source from list of objects.
 */
sealed class BundledWidgetSourceView : DefaultSearchItem {
    abstract val id: Id

    data object Favorites : BundledWidgetSourceView() {
        override val id: Id get() = BundledWidgetSourceIds.FAVORITE
    }
    data object Recent : BundledWidgetSourceView() {
        override val id: Id get() = BundledWidgetSourceIds.RECENT
    }

    data object RecentLocal : BundledWidgetSourceView() {
        override val id: Id get() = BundledWidgetSourceIds.RECENT_LOCAL
    }

    data object Bin : BundledWidgetSourceView() {
        override val id: Id get() = BundledWidgetSourceIds.BIN
    }

    data object AllObjects : BundledWidgetSourceView() {
        override val id: Id get() = BundledWidgetSourceIds.ALL_OBJECTS
    }

    data object Chat : BundledWidgetSourceView() {
        override val id: Id get() = BundledWidgetSourceIds.CHAT
    }
}

data class SuggestWidgetObjectType(
    val id: Id,
    val name: String,
    val objectIcon: ObjectIcon
) : DefaultSearchItem