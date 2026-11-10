package com.chronoio.chrono.domain.relations

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class DeleteRelationOptions @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<DeleteRelationOptions.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.DeleteRelationOptions(optionIds = params.optionIds)
        repo.deleteRelationOption(command)
    }

    data class Params(val optionIds: List<Id>)
}