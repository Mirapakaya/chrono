package com.chronoio.chrono.domain.favorites

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

class RemovePersonalFavorite @Inject constructor(
    private val repo: PersonalFavoritesRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<RemovePersonalFavorite.Params, Payload?>(dispatchers.io) {

    data class Params(
        val space: SpaceId,
        val target: Id
    )

    override suspend fun doWork(params: Params): Payload? =
        repo.remove(params.space, params.target)
}
