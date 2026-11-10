package com.chronoio.chrono.domain.templates

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.DVSort
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.ObjectTypeIds
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.RelationFormat
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.primitives.TypeId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.workspace.SpaceManager
import kotlinx.coroutines.withContext

/**
 * Use-case for getting templates for specific [ObjectType]
 */
class GetTemplates(
    private val repo: BlockRepository,
    private val spaceManager: SpaceManager,
    private val dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetTemplates.Params, List<ObjectWrapper.Basic>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<ObjectWrapper.Basic> {
        return withContext(dispatchers.io) {
            try {
                repo.searchObjects(
                    // TODO provide space id to params
                    space = SpaceId(spaceManager.get()),
                    filters = listOf(
                        DVFilter(
                            relation = Relations.IS_ARCHIVED,
                            condition = DVFilterCondition.NOT_EQUAL,
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
                        ),
                        DVFilter(
                            relation = Relations.TARGET_OBJECT_TYPE,
                            condition = DVFilterCondition.EQUAL,
                            value = params.type.id
                        ),
                        DVFilter(
                            relation = Relations.TYPE_UNIQUE_KEY,
                            condition = DVFilterCondition.EQUAL,
                            value = ObjectTypeIds.TEMPLATE
                        ),
                        DVFilter(
                            relation = Relations.ID,
                            condition = DVFilterCondition.NOT_EMPTY
                        )
                    ),
                    keys = listOf(
                        Relations.ID,
                        Relations.SPACE_ID,
                        Relations.NAME,
                        Relations.PLURAL_NAME,
                        Relations.LAYOUT,
                        Relations.ICON_EMOJI,
                        Relations.ICON_NAME,
                        Relations.ICON_IMAGE,
                        Relations.ICON_OPTION,
                        Relations.COVER_ID,
                        Relations.COVER_TYPE
                    ),
                    sorts = listOf(
                        DVSort(
                            relationKey = Relations.CREATED_DATE,
                            type = Block.Content.DataView.Sort.Type.DESC,
                            relationFormat = RelationFormat.DATE
                        )
                    ),
                    fulltext = "",
                    offset = 0,
                    limit = 100
                ).map { obj ->
                    ObjectWrapper.Basic(obj)
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * @property [type] id of the object type, whose templates we are interested in.
     */
    data class Params(val type: TypeId)
}