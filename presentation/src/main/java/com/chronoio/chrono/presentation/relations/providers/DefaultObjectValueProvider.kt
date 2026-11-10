package com.chronoio.chrono.presentation.relations.providers

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Struct
import com.chronoio.chrono.presentation.editor.editor.Store
import kotlin.collections.orEmpty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultObjectValueProvider(
    private val details: Store.Details
) : ObjectValueProvider {
    override suspend fun get(ctx: Id, target: Id): Struct {
        return details.current().details[target].orEmpty()
    }

    override suspend fun subscribe(ctx: Id, target: Id): Flow<Struct> {
        return details.stream().map { details ->
            details.details[target].orEmpty()
        }
    }
}