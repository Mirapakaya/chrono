package com.chronoio.chrono.domain.table

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetTableRowHeader(
    private val repo: BlockRepository
) : BaseUseCase<Payload, SetTableRowHeader.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.setTableRowHeader(
            ctx = params.ctx,
            targetId = params.row,
            isHeader = params.isHeader
        )
    }

    data class Params(
        val ctx: Id,
        val row: Id,
        val isHeader: Boolean
    )
}