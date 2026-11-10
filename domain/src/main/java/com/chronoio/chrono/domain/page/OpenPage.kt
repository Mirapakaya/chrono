package com.chronoio.chrono.domain.page

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.Result
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

open class OpenPage @Inject constructor(
    private val repo: BlockRepository,
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<OpenPage.Params, Result<Payload>>(dispatchers.io) {

    override suspend fun doWork(params: Params): Result<Payload> {
        return repo.openPage(
            id = params.obj,
            space = params.space
        )
    }

    data class Params(
        val obj: Id,
        val saveAsLastOpened: Boolean,
        val space: SpaceId
    )
}