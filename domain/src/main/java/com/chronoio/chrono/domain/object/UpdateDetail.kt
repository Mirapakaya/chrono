package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository

// TODO rename to SetObjectDetails
class UpdateDetail(private val repo: BlockRepository): BaseUseCase<Payload, UpdateDetail.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.setObjectDetail(
            ctx = params.target,
            key = params.key,
            value = params.value
        )
    }

    data class Params(
        val target: Id,
        val key: String,
        val value: Any?
    )
}