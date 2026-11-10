package com.chronoio.chrono.presentation.relations.providers

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.presentation.editor.Editor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class DefaultObjectRelationProvider(
    private val storeOfRelations: StoreOfRelations,
    private val storage: Editor.Storage
) : ObjectRelationProvider {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun observeAll(id: Id): Flow<List<ObjectWrapper.Relation>> =
        combine(
            storeOfRelations.trackChanges(),
            storage.details.stream()
        ) { _, details ->
            details
        }.flatMapLatest { details ->
            val objectKeys = details.details[id]?.keys.orEmpty()
            flow {
                objectKeys.mapNotNull {
                    storeOfRelations.getByKey(it)
                }
            }
        }
}