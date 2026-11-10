package com.chronoio.chrono.domain.all_content

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class UpdateAllContentState @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<UpdateAllContentState.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        settings.setAllContentSort(
            space = params.spaceId,
            sort = params.sort,
            isAsc = params.isAsc
        )
    }

    data class Params(
        val spaceId: SpaceId,
        val sort: Id,
        val isAsc: Boolean
    )
}