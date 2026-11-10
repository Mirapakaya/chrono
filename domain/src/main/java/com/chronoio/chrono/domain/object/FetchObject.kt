package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Key
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class FetchObject @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<FetchObject.Params, ObjectWrapper.Basic?>(dispatchers.io) {

    override suspend fun doWork(params: Params): ObjectWrapper.Basic? {
        val result = repo.searchObjects(
            filters = buildList {
                add(
                    DVFilter(
                        relation = Relations.ID,
                        value = params.obj,
                        condition = DVFilterCondition.EQUAL
                    )
                )
            },
            limit = 1,
            keys = params.keys,
            space = params.space
        )
        return if (result.isNotEmpty() && result.first().isNotEmpty()) {
            ObjectWrapper.Basic(result.first())
        } else {
            null
        }
    }

    data class Params(
        val space: SpaceId,
        val obj: Id,
        val keys: List<Key> = listOf(
            Relations.ID,
            Relations.SPACE_ID,
            Relations.LAYOUT,
            Relations.IS_ARCHIVED,
            Relations.IS_DELETED,
            Relations.IS_HIDDEN
        )
    )
}