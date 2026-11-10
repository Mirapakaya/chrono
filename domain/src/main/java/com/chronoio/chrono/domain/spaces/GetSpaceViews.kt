package com.chronoio.chrono.domain.spaces

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.DVSort
import com.chronoio.chrono.core_models.DVSortType
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.RelationFormat
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.restrictions.SpaceStatus
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetSpaceViews @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<SpaceId, List<ObjectWrapper.SpaceView>>(dispatchers.io) {
    override suspend fun doWork(params: SpaceId): List<ObjectWrapper.SpaceView> {
        val result = repo.searchObjects(
            space = params,
            keys = listOf(
                Relations.ID,
                Relations.TARGET_SPACE_ID,
                Relations.NAME,
                Relations.LAYOUT,
                Relations.ICON_IMAGE,
                Relations.ICON_OPTION,
                Relations.SPACE_ACCOUNT_STATUS,
                Relations.SPACE_LOCAL_STATUS,
                Relations.LAST_OPENED_DATE
            ),
            filters = listOf(
                DVFilter(
                    relation = Relations.LAYOUT,
                    value = ObjectType.Layout.SPACE_VIEW.code.toDouble(),
                    condition = DVFilterCondition.EQUAL
                ),
                DVFilter(
                    relation = Relations.SPACE_ACCOUNT_STATUS,
                    value = buildList {
                        add(SpaceStatus.SPACE_DELETED.code.toDouble())
                        add(SpaceStatus.SPACE_REMOVING.code.toDouble())
                    },
                    condition = DVFilterCondition.NOT_IN
                ),
                DVFilter(
                    relation = Relations.SPACE_LOCAL_STATUS,
                    value = SpaceStatus.OK.code.toDouble(),
                    condition = DVFilterCondition.EQUAL
                )
            ),
            sorts = listOf(
                DVSort(
                    relationKey = Relations.LAST_OPENED_DATE,
                    type = DVSortType.DESC,
                    includeTime = true,
                    relationFormat = RelationFormat.DATE
                )
            ),
            limit = 0
        )
        return result.map {
            ObjectWrapper.SpaceView(it)
        }
    }
}