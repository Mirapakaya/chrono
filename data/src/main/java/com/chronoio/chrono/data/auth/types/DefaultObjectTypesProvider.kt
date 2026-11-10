package com.chronoio.chrono.data.auth.types

import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.domain.`object`.ObjectTypesProvider

class DefaultObjectTypesProvider : ObjectTypesProvider {

    private val objectTypes: MutableList<ObjectType> = mutableListOf()

    override fun get(): List<ObjectType> = objectTypes

    override fun set(objectTypes: List<ObjectType>) {
        this.objectTypes.apply {
            clear()
            addAll(objectTypes)
        }
    }
}