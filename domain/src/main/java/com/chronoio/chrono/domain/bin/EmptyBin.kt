package com.chronoio.chrono.domain.bin

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.workspace.SpaceManager

class EmptyBin(
    private val repo: BlockRepository,
    private val spaceManager: SpaceManager,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, List<Id>>(dispatchers.io) {
    override suspend fun doWork(params: Unit) : List<Id> {
        val archived = repo.searchObjects(
            // TODO DROID-2916 Move space id to use case params
            space = SpaceId(spaceManager.get()),
            filters = listOf(
                DVFilter(
                    relation = Relations.IS_ARCHIVED,
                    condition = DVFilterCondition.EQUAL,
                    value = true
                ),
                DVFilter(
                    relation = Relations.IS_DELETED,
                    condition = DVFilterCondition.NOT_EQUAL,
                    value = true
                ),
                DVFilter(
                    relation = Relations.IS_HIDDEN,
                    condition = DVFilterCondition.NOT_EQUAL,
                    value = true
                ),
                DVFilter(
                    relation = Relations.IS_HIDDEN_DISCOVERY,
                    condition = DVFilterCondition.NOT_EQUAL,
                    value = true
                )
            ),
            sorts = emptyList(),
            fulltext = "",
            limit = 0,
            offset = 0
        ).map { struct ->
            ObjectWrapper.Basic(struct).id
        }
        repo.deleteObjects(
            targets = archived
        )
        return archived
    }
}