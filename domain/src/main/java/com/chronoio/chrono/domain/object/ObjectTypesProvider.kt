package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.ObjectType

@Deprecated("To be deleted")
interface ObjectTypesProvider {
    fun get(): List<ObjectType>
    fun set(objectTypes: List<ObjectType>)
}