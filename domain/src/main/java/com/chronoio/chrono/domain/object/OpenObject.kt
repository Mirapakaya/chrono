package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectView
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class OpenObject @Inject constructor(
    private val repo: BlockRepository,
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<OpenObject.Params, ObjectView>(dispatchers.io) {

    override suspend fun doWork(params: Params) = repo.openObject(
        id = params.obj,
        space = params.spaceId
    )

    data class Params(
        val obj: Id,
        val saveAsLastOpened: Boolean = true,
        val spaceId: SpaceId
    )
}