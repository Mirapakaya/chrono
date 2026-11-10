package com.chronoio.chrono.core_models

import com.chronoio.chrono.core_models.restrictions.DataViewRestrictions
import com.chronoio.chrono.core_models.restrictions.ObjectRestriction

data class ObjectView(
    val root: Id,
    val blocks: List<Block>,
    val details: Map<Id, Struct>,
    val objectRestrictions: List<ObjectRestriction>,
    val dataViewRestrictions: List<DataViewRestrictions>
)