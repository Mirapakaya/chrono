package com.chronoio.chrono.domain.media

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Preloads file for uploading.
 * @return id of the file
 * @see [Params]
 */
class PreloadFile @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<PreloadFile.Params, Id>(dispatchers.io) {

    override suspend fun doWork(params: Params) : Id = repo.preloadFile(
        command = Command.PreloadFile(
            path = params.path,
            type = params.type,
            space = params.space
        )
    )

    data class Params(
        val path: String,
        val space: SpaceId,
        val type: Block.Content.File.Type = Block.Content.File.Type.FILE
    )
}