package com.chronoio.chrono.domain.misc

import com.chronoio.chrono.core_models.primitives.TypeKey

interface AppActionManager {

    fun setup(action: Action)

    fun setup(actions: List<Action.CreateNew>)

    sealed class Action {
        data class CreateNew(val type: TypeKey, val name: String) : Action()
        object ClearAll: Action()
    }
}