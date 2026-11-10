package com.chronoio.chrono.domain.relations

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository

class CreateRelationOption(
    private val repo: BlockRepository
) : BaseUseCase<ObjectWrapper.Option, CreateRelationOption.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.createRelationOption(
            space = params.space,
            name = params.name,
            relation = params.relation,
            color = params.color
        )
    }

    /**
     * [relation] relation key or id for new option
     */
    data class Params(
        val space: Id,
        val relation: Id,
        val name: String,
        val color: String,
    )
}