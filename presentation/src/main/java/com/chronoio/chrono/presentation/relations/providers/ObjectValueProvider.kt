package com.chronoio.chrono.presentation.relations.providers

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Struct
import kotlinx.coroutines.flow.Flow

interface ObjectValueProvider {
    suspend fun get(ctx: Id, target: Id): Struct
    suspend fun subscribe(ctx: Id, target: Id) : Flow<Struct>
}