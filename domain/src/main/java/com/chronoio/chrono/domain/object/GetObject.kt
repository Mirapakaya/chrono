package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectView
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

/**
 * Use-case for opening an object as preview — without subscribing to its subsequent changes.
 * If you want to receive payload events, you should use [OpenObject] instead.
 */
class GetObject @Inject constructor(
    private val repo: BlockRepository,
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetObject.Params, ObjectView>(dispatchers.io) {
    override suspend fun doWork(params: Params): ObjectView = repo.getObject(
        id = params.target,
        space = params.space
    )

    data class Params(
        val target: Id,
        val space: SpaceId,
        val saveAsLastOpened: Boolean = false
    )
}