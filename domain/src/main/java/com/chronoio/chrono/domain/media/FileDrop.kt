package com.chronoio.chrono.domain.media

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.NO_VALUE
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Position
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class FileDrop @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<FileDrop.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload = repo.fileDrop(
        command = Command.FileDrop(
            ctx = params.ctx,
            space = params.space,
            dropTarget = params.dropTarget,
            blockPosition = params.blockPosition,
            localFilePaths = params.localFilePaths
        )
    )

    data class Params(
        val space: SpaceId,
        val ctx: Id,
        val dropTarget: Id = NO_VALUE,
        val blockPosition: Position = Position.NONE,
        val localFilePaths: List<String>
    )
}