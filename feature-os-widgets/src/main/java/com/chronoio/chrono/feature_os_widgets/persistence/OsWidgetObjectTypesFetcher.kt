package com.chronoio.chrono.feature_os_widgets.persistence

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.ObjectTypeUniqueKeys
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.ext.mapToObjectWrapperType
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.presentation.search.ObjectSearchConstants
import timber.log.Timber

/**
 * Shared one-shot fetcher for object types in a space.
 * Used by OS widget config/view sync flows to resolve type names/icons.
 */
suspend fun fetchObjectTypesForSpace(
    searchObjects: SearchObjects,
    spaceId: SpaceId
): Map<Id, ObjectWrapper.Type> {
    val filters = buildList {
        add(
            DVFilter(
                relation = Relations.IS_DELETED,
                condition = DVFilterCondition.NOT_EQUAL,
                value = true
            )
        )
        add(
            DVFilter(
                relation = Relations.IS_ARCHIVED,
                condition = DVFilterCondition.NOT_EQUAL,
                value = true
            )
        )
        add(
            DVFilter(
                relation = Relations.TYPE_UNIQUE_KEY,
                condition = DVFilterCondition.NOT_EQUAL,
                value = ObjectTypeUniqueKeys.TEMPLATE
            )
        )
        add(
            DVFilter(
                relation = Relations.LAYOUT,
                condition = DVFilterCondition.EQUAL,
                value = ObjectType.Layout.OBJECT_TYPE.code.toDouble()
            )
        )
        add(
            DVFilter(
                relation = Relations.UNIQUE_KEY,
                condition = DVFilterCondition.NOT_EMPTY
            )
        )
    }

    val params = SearchObjects.Params(
        space = spaceId,
        filters = filters,
        sorts = emptyList(),
        keys = ObjectSearchConstants.defaultKeysObjectType,
        limit = 0
    )

    return try {
        val results = searchObjects(params).getOrNull() ?: emptyList()
        results.mapNotNull { obj ->
            obj.map.mapToObjectWrapperType()?.let { type ->
                type.id to type
            }
        }.toMap()
    } catch (e: Exception) {
        Timber.e(e, "Error fetching object types for space")
        emptyMap()
    }
}
