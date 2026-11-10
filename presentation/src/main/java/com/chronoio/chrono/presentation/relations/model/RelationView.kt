package com.chronoio.chrono.presentation.relations.model

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Key
import com.chronoio.chrono.core_models.RelationFormat


sealed class RelationItemView

sealed class RelationView : RelationItemView() {

    abstract val format: RelationFormat

    data class Existing(
        val id: Id,
        val key: Key,
        val space: Id?,
        val name: String,
        override val format: RelationFormat,
    ) : RelationView()

    data class CreateFromScratch(
        override val format: RelationFormat,
        val isSelected: Boolean = false
    ) : RelationView()
}

sealed class Section : RelationItemView() {
    object Library: Section()
    object Marketplace : Section()
}