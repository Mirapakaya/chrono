package com.chronoio.chrono.domain.search

import com.chronoio.chrono.core_models.GlobalSearchHistory
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class UpdateGlobalSearchHistory @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<UpdateGlobalSearchHistory.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        settings.setGlobalSearchHistory(
            globalSearchHistory = GlobalSearchHistory(
                query = params.query,
                relatedObject = params.relatedObjectId
            ),
            space = params.spaceId
        )
    }

    data class Params(
        val spaceId: SpaceId,
        val query: String,
        val relatedObjectId: Id?
    )
}