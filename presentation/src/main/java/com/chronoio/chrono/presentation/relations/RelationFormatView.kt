package com.chronoio.chrono.presentation.relations

import com.chronoio.chrono.core_models.Relation

data class RelationFormatView(
    val format: Relation.Format,
    val isSelected: Boolean = false
)