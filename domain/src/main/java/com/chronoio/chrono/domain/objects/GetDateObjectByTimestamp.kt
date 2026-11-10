package com.chronoio.chrono.domain.objects

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Struct
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.objects.GetDateObjectByTimestamp.Params
import javax.inject.Inject

class GetDateObjectByTimestamp @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Params, Struct?>(dispatchers.io) {

    override suspend fun doWork(params: Params): Struct? {
        val command = Command.ObjectDateByTimestamp(
            space = params.space,
            timeInSeconds = params.timestampInSeconds
        )
        return repo.objectDateByTimestamp(command)
    }

    data class Params(val space: SpaceId, val timestampInSeconds: Long)
}