package com.chronoio.chrono.domain.gallery_experience

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class ImportExperience @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<ImportExperience.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.ImportExperience(
            space = params.spaceId,
            url = params.url,
            title = params.title,
            isNewSpace = params.isNewSpace
        )
        repo.importExperience(command)
    }

    data class Params(
        val spaceId: SpaceId,
        val url: String,
        val title: String,
        val isNewSpace: Boolean
    )
}