package com.chronoio.chrono.domain.notifications

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class ReplyNotifications @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers,
): ResultInteractor<List<Id>, Unit>(dispatchers.io) {
    override suspend fun doWork(params: List<Id>) {
        repo.replyNotifications(notifications = params)
    }
}