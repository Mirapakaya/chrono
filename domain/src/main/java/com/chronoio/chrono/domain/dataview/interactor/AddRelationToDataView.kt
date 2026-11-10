package com.chronoio.chrono.domain.dataview.interactor

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Key
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for adding an existing relation to a data view.
 */
class AddRelationToDataView(
    private val repo: BlockRepository
) : BaseUseCase<Payload, AddRelationToDataView.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.addRelationToDataView(
            ctx = params.ctx,
            dv = params.dv,
            relation = params.relation
        )
    }

    /**
     * @property [ctx] operation context
     * @property [dv] data view's block id
     * @property [relation] id or key of the existing relation
     */
    class Params(
        val ctx: Id,
        val dv: Id,
        val relation: Key
    )
}