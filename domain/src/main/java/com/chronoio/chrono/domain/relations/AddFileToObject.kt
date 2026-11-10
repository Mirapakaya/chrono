package com.chronoio.chrono.domain.relations

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.ext.addIds
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class AddFileToObject(
    private val repo: BlockRepository
) : BaseUseCase<Payload, AddFileToObject.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        val file = repo.uploadFile(
            command = Command.UploadFile(
                path = params.path,
                type = null,
                space = params.space,
                createdInContext = params.ctx,
                createdInContextRef = params.relation
            )
        )
        val obj = params.obj
        val remaining = obj[params.relation].addIds(listOf(file.id))
        repo.setObjectDetail(
            ctx = params.ctx,
            key = params.relation,
            value = remaining
        )
    }

    class Params(
        val ctx: Id,
        val relation: Id,
        val obj: Map<String, Any?>,
        val path: String,
        val space: SpaceId
    )
}