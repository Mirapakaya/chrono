package com.chronoio.chrono.feature_object_type.viewmodel

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.DVSort
import com.chronoio.chrono.core_models.DVSortType
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.ObjectTypeUniqueKeys
import com.chronoio.chrono.core_models.RelationFormat
import com.chronoio.chrono.core_models.Relations

fun filtersForSearch(
    objectTypeId: Id
): List<DVFilter> {
    val filters = buildList {
        addAll(buildDeletedFilter())
        add(buildTemplateFilter())
        add(buildTypeIdFilter(listOf(objectTypeId)))
    }
    return filters
}

fun filtersForSetsSearch(
    objectTypeId: Id
): List<DVFilter> {
    val filters = buildList {
        addAll(buildDeletedFilter())
        add(
            DVFilter(
                relation = Relations.LAYOUT,
                condition = DVFilterCondition.IN,
                value = listOf(ObjectType.Layout.SET.code.toDouble())
            )
        )
        add(
            DVFilter(
                relation = Relations.SET_OF,
                condition = DVFilterCondition.EQUAL,
                value = objectTypeId
            )
        )
    }
    return filters
}

fun filtersForTemplatesSearch(
    objectTypeId: Id
): List<DVFilter> {
    val filters = buildList {
        addAll(buildDeletedFilter())
        add(
            DVFilter(
                relation = Relations.TYPE_UNIQUE_KEY,
                condition = DVFilterCondition.EQUAL,
                value = ObjectTypeUniqueKeys.TEMPLATE
            )
        )
        add(
            DVFilter(
                relation = Relations.TARGET_OBJECT_TYPE,
                condition = DVFilterCondition.EQUAL,
                value = objectTypeId
            )
        )
    }
    return filters
}

fun sortForSetSearch() = DVSort(
    relationKey = Relations.CREATED_DATE,
    type = DVSortType.DESC,
    includeTime = true,
    relationFormat = RelationFormat.DATE
)

fun sortForTemplatesSearch() = DVSort(
    relationKey = Relations.LAST_MODIFIED_DATE,
    type = DVSortType.DESC,
    includeTime = true,
    relationFormat = RelationFormat.DATE
)

private fun buildTypeIdFilter(ids: List<Id>): DVFilter = DVFilter(
    relation = Relations.TYPE,
    condition = DVFilterCondition.IN,
    value = ids
)


private fun buildDeletedFilter(): List<DVFilter> {
    return listOf(
        DVFilter(
            relation = Relations.IS_ARCHIVED,
            condition = DVFilterCondition.NOT_EQUAL,
            value = true
        ),
        DVFilter(
            relation = Relations.IS_HIDDEN,
            condition = DVFilterCondition.NOT_EQUAL,
            value = true
        ),
        DVFilter(
            relation = Relations.IS_DELETED,
            condition = DVFilterCondition.NOT_EQUAL,
            value = true
        ),
        DVFilter(
            relation = Relations.IS_HIDDEN_DISCOVERY,
            condition = DVFilterCondition.NOT_EQUAL,
            value = true
        )
    )
}

private fun buildSpaceIdFilter(spaces: List<Id>): DVFilter = DVFilter(
    relation = Relations.SPACE_ID,
    condition = DVFilterCondition.IN,
    value = spaces
)

private fun buildTemplateFilter(): DVFilter = DVFilter(
    relation = Relations.TYPE_UNIQUE_KEY,
    condition = DVFilterCondition.NOT_EQUAL,
    value = ObjectTypeUniqueKeys.TEMPLATE
)