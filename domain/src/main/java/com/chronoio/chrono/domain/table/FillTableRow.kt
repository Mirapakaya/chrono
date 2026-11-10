package com.chronoio.chrono.domain.table

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class FillTableRow(
    private val repo: BlockRepository
) : BaseUseCase<Payload, FillTableRow.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.fillTableRow(
            ctx = params.ctx,
            targetIds = params.targetIds
        )
    }

    /**
     * @property [targetIds] the list of rows that need to be filled in
     */
    data class Params(
        val ctx: Id,
        val targetIds: List<Id>
    )
}