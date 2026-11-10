package com.chronoio.chrono.feature_os_widgets.mapper

import com.chronoio.chrono.core_models.SystemColor
import com.chronoio.chrono.feature_os_widgets.model.OsWidgetSpaceIcon
import com.chronoio.chrono.feature_os_widgets.model.OsWidgetSpaceItem
import com.chronoio.chrono.feature_os_widgets.persistence.OsWidgetSpaceEntity

/**
 * Maps persistence entity to domain/UI model.
 */
fun OsWidgetSpaceEntity.toDomain(): OsWidgetSpaceItem {
    val color = SystemColor.color(iconColorIndex)
    val imageUrl = iconImageUrl
    val icon = if (imageUrl != null) {
        OsWidgetSpaceIcon.Image(url = imageUrl, color = color)
    } else {
        OsWidgetSpaceIcon.Placeholder(color = color, name = name)
    }

    return OsWidgetSpaceItem(
        spaceId = spaceId,
        name = name,
        icon = icon,
        isOneToOneSpace = isOneToOneSpace
    )
}

/**
 * Maps a list of entities to domain models.
 */
fun List<OsWidgetSpaceEntity>.toDomain(): List<OsWidgetSpaceItem> = map { it.toDomain() }
