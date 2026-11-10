package com.chronoio.chrono.domain.dataview

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SetDataViewQuery(private val repo: BlockRepository) :
    BaseUseCase<Payload, SetDataViewQuery.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.blockDataViewSetSource(
            ctx = params.ctx,
            block = DEFAULT_DATA_VIEW_BLOCK_ID,
            sources = params.sources
        )
    }

    companion object {
        const val DEFAULT_DATA_VIEW_BLOCK_ID = "dataview"
    }

    data class Params(
        val ctx: Id,
        val sources: List<Id>
    )
}