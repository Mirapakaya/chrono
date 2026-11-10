package com.chronoio.chrono.domain.search

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVSort
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SearchObjects @Inject constructor(
    private val repo: BlockRepository
) : BaseUseCase<List<ObjectWrapper.Basic>, SearchObjects.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.searchObjects(
            space = params.space,
            sorts = params.sorts,
            filters = params.filters,
            fulltext = params.fulltext,
            offset = params.offset,
            limit = params.limit,
            keys = params.keys
        ).map { response ->
            ObjectWrapper.Basic(response)
        }
    }

    data class Params(
        val space: SpaceId,
        val sorts: List<DVSort> = emptyList(),
        val filters: List<DVFilter> = emptyList(),
        val fulltext: String = EMPTY_TEXT,
        val offset: Int = INIT_OFFSET,
        val limit: Int = LIMIT,
        val keys: List<Id> = emptyList()
    )

    companion object {
        const val EMPTY_TEXT = ""
        const val LIMIT = 1000
        const val INIT_OFFSET = 0
    }
}