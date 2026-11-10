package com.chronoio.chrono.domain.objects

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Url
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreateObjectFromUrl @Inject constructor(
    private val repository: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateObjectFromUrl.Params, ObjectWrapper.Basic>(dispatchers.io) {

    override suspend fun doWork(params: Params): ObjectWrapper.Basic {
        return repository.createObjectFromUrl(
            space = params.space,
            url = params.url,
            createdInContext = params.createdInContext
        )
    }

    data class Params(
        val space: SpaceId,
        val url: Url,
        val createdInContext: Id? = null
    )
}