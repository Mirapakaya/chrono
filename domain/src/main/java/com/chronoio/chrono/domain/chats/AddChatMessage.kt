package com.chronoio.chrono.domain.chats

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Event
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * returns message ID and payload commands.
 */
class AddChatMessage @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Command.ChatCommand.AddMessage, Pair<Id, List<Event.Command.Chats>>>(dispatchers.io) {

    override suspend fun doWork(params: Command.ChatCommand.AddMessage): Pair<Id, List<Event.Command.Chats>> {
        return repo.addChatMessage(params)
    }
}