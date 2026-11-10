package com.chronoio.chrono.domain.block.interactor

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload

class UpdateFields(private val repo: BlockRepository) :
    BaseUseCase<Payload, UpdateFields.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.setFields(
            command = Command.SetFields(
                context = params.context,
                fields = params.fields
            )
        )
    }

    data class Params(
        val context: Id,
        val fields: List<Pair<Id, Block.Fields>>
    )
}