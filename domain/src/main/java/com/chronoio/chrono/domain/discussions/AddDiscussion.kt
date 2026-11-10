package com.chronoio.chrono.domain.discussions

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class AddDiscussion @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Id, Id>(dispatchers.io) {

    override suspend fun doWork(params: Id): Id {
        return repo.addDiscussion(params)
    }
}
