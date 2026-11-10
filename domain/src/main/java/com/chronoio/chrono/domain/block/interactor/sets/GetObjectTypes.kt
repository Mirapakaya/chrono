package com.chronoio.chrono.domain.block.interactor.sets

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVSort
import com.chronoio.chrono.core_models.Key
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.ext.mapToObjectWrapperType
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetObjectTypes @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetObjectTypes.Params, List<ObjectWrapper.Type>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<ObjectWrapper.Type> {
        val result = repo.searchObjects(
            space = params.space,
            keys = params.keys,
            filters = params.filters,
            sorts = params.sorts,
            limit = params.limit,
            offset = params.offset,
            fulltext = params.query
        )
        return result.mapNotNull { it.mapToObjectWrapperType() }
    }

    data class Params(
        val space: SpaceId,
        val sorts: List<DVSort> = emptyList(),
        val filters: List<DVFilter> = emptyList(),
        val keys: List<Key> = emptyList(),
        val offset: Int = 0,
        val limit: Int = 0,
        val query: String = ""
    )
}