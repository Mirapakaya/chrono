package com.chronoio.chrono.domain.media

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class UploadFile @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<UploadFile.Params, ObjectWrapper.File>(dispatchers.io) {

    override suspend fun doWork(params: Params) : ObjectWrapper.File = repo.uploadFile(
        command = Command.UploadFile(
            path = params.path,
            type = params.type,
            space = params.space,
            preloadFileId = params.preloadFileId,
            createdInContext = params.createdInContext,
            createdInContextRef = params.createdInContextRef
        )
    )

    data class Params(
        val path: String,
        val space: SpaceId,
        val type: Block.Content.File.Type = Block.Content.File.Type.FILE,
        val preloadFileId: Id? = null,
        val createdInContext: Id? = null,
        val createdInContextRef: String? = null
    )
}