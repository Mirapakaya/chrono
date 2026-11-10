package com.chronoio.chrono.domain.all_content

import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class RestoreAllContentState @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<RestoreAllContentState.Params, RestoreAllContentState.Response>(
    dispatchers.io
) {

    override suspend fun doWork(params: Params): Response {
        val res = settings.getAllContentSort(params.spaceId)
        return if (res == null) {
            Response.Empty
        } else {
            Response.Success(activeSort = res.first, isAsc = res.second)
        }
    }

    data class Params(
        val spaceId: SpaceId
    )

    sealed class Response {
        data class Success(
            val activeSort: String,
            val isAsc: Boolean
        ) : Response()
        data object Empty : Response()
    }
}