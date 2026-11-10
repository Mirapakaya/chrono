package com.chronoio.chrono.domain.chats

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class DeleteChatMessage @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Command.ChatCommand.DeleteMessage, Unit>(dispatchers.io) {
    override suspend fun doWork(params: Command.ChatCommand.DeleteMessage) {
        return repo.deleteChatMessage(command = params)
    }
}