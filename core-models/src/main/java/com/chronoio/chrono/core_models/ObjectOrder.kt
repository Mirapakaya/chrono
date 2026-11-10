package com.chronoio.chrono.core_models

data class ObjectOrder(
    val view: Id,
    val group: Id,
    val ids: List<Id>
)
