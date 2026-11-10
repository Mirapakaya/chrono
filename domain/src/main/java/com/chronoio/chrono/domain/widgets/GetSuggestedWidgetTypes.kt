package com.chronoio.chrono.domain.widgets

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.ObjectTypeUniqueKeys
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.ext.asMap
import com.chronoio.chrono.core_models.primitives.Space
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.widgets.BundledWidgetSourceIds
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetSuggestedWidgetTypes @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository,
) : ResultInteractor<GetSuggestedWidgetTypes.Params, GetSuggestedWidgetTypes.Result>(dispatchers.io) {

    override suspend fun doWork(params: Params): Result {

        val (alreadyUsedObjectTypes, alreadyUsedSystemSources) = getAlreadyUsedTypes(
            space = params.space,
            widgets = params.ctx
        )

        val types = repo.searchObjects(
            space = params.space,
            limit = DEFAULT_LIMIT,
            keys = params.objectTypeKeys,
            filters = buildList {
                addAll(params.objectTypeFilters)
                if (alreadyUsedObjectTypes.isNotEmpty()) {
                    add(
                        DVFilter(
                            relation = Relations.ID,
                            condition = DVFilterCondition.NOT_IN,
                            value = alreadyUsedObjectTypes
                        )
                    )
                }
                add(
                    DVFilter(
                        relation = Relations.UNIQUE_KEY,
                        condition = DVFilterCondition.NOT_EQUAL,
                        value = ObjectTypeUniqueKeys.OBJECT_TYPE
                    )
                )
            }
        ).map { result ->
            ObjectWrapper.Type(result)
        }

        return Result(
            suggestedObjectTypes = types,
            suggestedSystemSources = BundledWidgetSourceIds.ids - alreadyUsedSystemSources
        )
    }

    private suspend fun getAlreadyUsedTypes(space: SpaceId, widgets: Id) : Pair<List<Id>, List<Id>> {
        val usedObjectTypes = mutableListOf<Id>()
        val usedSystemSources = mutableListOf<Id>()

        runCatching {
            val preview = repo.getObject(space = space, id = widgets)

            val map = preview.blocks.asMap()

            map.getOrDefault(widgets, emptyList()).forEach { block ->
                if (block.content is Block.Content.Widget && block.children.isNotEmpty()) {
                    val link = preview.blocks.find { it.id == block.children.first() }
                    val content = link?.content
                    if (content is Block.Content.Link) {
                        val source = preview.details.getOrDefault(
                            content.target,
                            emptyMap()
                        )
                        val wrapper = ObjectWrapper.Basic(source)
                        if (wrapper.layout == ObjectType.Layout.OBJECT_TYPE) {
                            usedObjectTypes.add(wrapper.id)
                        } else {
                            if (BundledWidgetSourceIds.ids.contains(content.target)) {
                                usedSystemSources.add(content.target)
                            }
                        }
                    }
                }
            }
        }

        return usedObjectTypes.distinct() to usedSystemSources.distinct()
    }


    data class Params(
        val space: Space,
        val ctx: Id,
        val objectTypeFilters: List<DVFilter>,
        val objectTypeKeys: List<Id>
    )

    data class Result(
        val suggestedObjectTypes: List<ObjectWrapper.Type>,
        val suggestedSystemSources: List<String>
    )
    
    companion object {
        const val DEFAULT_LIMIT = 10
    }
}