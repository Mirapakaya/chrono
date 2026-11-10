package com.chronoio.chrono.domain.dataview

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Key
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SetDataViewProperties @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SetDataViewProperties.Params, Payload>(dispatchers.io) {

    override suspend fun doWork(params: Params): Payload {
        val params = Command.SetDataViewProperties(
            objectId = params.objectId,
            blockId = DEFAULT_DATA_VIEW_BLOCK_ID,
            properties = params.properties
        )
        return repo.setDataViewProperties(command = params)
    }

    companion object {
        const val DEFAULT_DATA_VIEW_BLOCK_ID = "dataview"
    }

    data class Params(
        val objectId: String,
        val properties: List<Key>
    )
}