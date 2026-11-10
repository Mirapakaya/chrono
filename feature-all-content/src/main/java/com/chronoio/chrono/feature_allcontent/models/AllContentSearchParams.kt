package com.chronoio.chrono.feature_allcontent.models

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVSort
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.library.StoreSearchParams
import com.chronoio.chrono.presentation.objects.ObjectsListSort
import com.chronoio.chrono.presentation.objects.toDVSort
import com.chronoio.chrono.presentation.search.ObjectSearchConstants.defaultKeys
import com.chronoio.chrono.presentation.search.buildChatsFilter
import com.chronoio.chrono.presentation.search.buildDeletedFilter
import com.chronoio.chrono.presentation.search.buildLayoutFilter
import com.chronoio.chrono.presentation.search.buildLimitedObjectIdsFilter
import com.chronoio.chrono.presentation.search.buildSpaceIdFilter
import com.chronoio.chrono.presentation.search.buildTemplateFilter
import com.chronoio.chrono.presentation.search.buildUnlinkedObjectFilter

val allContentTabLayouts = mapOf(
    AllContentTab.PAGES to listOf(
        ObjectType.Layout.BASIC,
        ObjectType.Layout.PROFILE,
        ObjectType.Layout.TODO,
        ObjectType.Layout.NOTE
    ),
    AllContentTab.LISTS to listOf(
        ObjectType.Layout.SET,
        ObjectType.Layout.COLLECTION,
        ObjectType.Layout.OBJECT_TYPE,
    ),
    AllContentTab.FILES to listOf(
        ObjectType.Layout.FILE,
        ObjectType.Layout.PDF
    ),
    AllContentTab.MEDIA to listOf(
        ObjectType.Layout.IMAGE,
        ObjectType.Layout.VIDEO,
        ObjectType.Layout.AUDIO
    ),
    AllContentTab.BOOKMARKS to listOf(
        ObjectType.Layout.BOOKMARK
    )
)

// Function to create subscription params
fun createSubscriptionParams(
    spaceId: Id,
    activeMode: UiTitleState,
    activeTab: AllContentTab,
    activeSort: ObjectsListSort,
    limitedObjectIds: List<String>,
    limit: Int,
    subscriptionId: String
): StoreSearchParams {
    val (filters, sorts) = activeTab.filtersForSubscribe(
        spaces = listOf(spaceId),
        activeSort = activeSort,
        limitedObjectIds = limitedObjectIds,
        activeMode = activeMode
    )
    return StoreSearchParams(
        space = SpaceId(spaceId),
        filters = filters,
        sorts = sorts,
        keys = defaultKeys,
        limit = limit,
        subscription = subscriptionId
    )
}

fun AllContentTab.filtersForSubscribe(
    spaces: List<Id>,
    activeSort: ObjectsListSort,
    limitedObjectIds: List<Id>,
    activeMode: UiTitleState
): Pair<List<DVFilter>, List<DVSort>> {
    val tab = this
    when (this) {
        AllContentTab.PAGES,
        AllContentTab.LISTS,
        AllContentTab.FILES,
        AllContentTab.MEDIA,
        AllContentTab.BOOKMARKS -> {
            val filters = buildList {
                addAll(buildDeletedFilter())
                add(buildLayoutFilter(layouts = allContentTabLayouts.getValue(tab)))
                add(buildSpaceIdFilter(spaces))
                add(buildTemplateFilter())
                add(buildChatsFilter())
                if (limitedObjectIds.isNotEmpty()) {
                    add(buildLimitedObjectIdsFilter(limitedObjectIds = limitedObjectIds))
                }
                if (activeMode == UiTitleState.OnlyUnlinked) {
                    addAll(buildUnlinkedObjectFilter())
                }
            }
            val sorts = listOf(activeSort.toDVSort())
            return filters to sorts
        }
    }
}

fun AllContentTab.filtersForSearch(
    spaces: List<Id>
): List<DVFilter> {
    val tab = this
    val filters = buildList {
        addAll(buildDeletedFilter())
        add(buildSpaceIdFilter(spaces))
        add(buildTemplateFilter())
    }
    return filters
}