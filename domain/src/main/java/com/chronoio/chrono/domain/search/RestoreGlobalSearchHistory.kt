package com.chronoio.chrono.domain.search

import com.chronoio.chrono.core_models.GlobalSearchHistory
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class RestoreGlobalSearchHistory @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<RestoreGlobalSearchHistory.Params, RestoreGlobalSearchHistory.Response>(
    dispatchers.io
) {

    override suspend fun doWork(params: Params): Response {
        return Response(
            globalSearchHistory = settings.setGlobalSearchHistory(
                space = params.spaceId
            )
        )
    }

    data class Params(
        val spaceId: SpaceId
    )

    data class Response(
        val globalSearchHistory: GlobalSearchHistory?
    )
}