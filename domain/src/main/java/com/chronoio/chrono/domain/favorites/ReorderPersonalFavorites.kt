package com.chronoio.chrono.domain.favorites

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

class ReorderPersonalFavorites @Inject constructor(
    private val repo: PersonalFavoritesRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ReorderPersonalFavorites.Params, List<Payload>>(dispatchers.io) {

    data class Params(
        val space: SpaceId,
        val order: List<Id>
    )

    override suspend fun doWork(params: Params): List<Payload> =
        repo.reorder(params.space, params.order)
}
