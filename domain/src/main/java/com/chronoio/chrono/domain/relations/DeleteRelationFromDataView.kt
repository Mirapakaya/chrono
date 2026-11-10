package com.chronoio.chrono.domain.relations

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for deleting a relation from a data view.
 */
class DeleteRelationFromDataView(
    private val repo: BlockRepository
) : BaseUseCase<Payload, DeleteRelationFromDataView.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.deleteRelationFromDataView(
            ctx = params.ctx,
            relation = params.relation,
            dv = params.dv
        )
    }

    /**
     * @param ctx id of the object
     * @param relation relation id or key
     */
    class Params(
        val ctx: Id,
        val dv: Id,
        val relation: Id
    )
}