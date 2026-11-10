package com.chronoio.chrono.domain.chats

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.chats.Chat
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetChatMessagesByIds @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Command.ChatCommand.GetMessagesByIds, List<Chat.Message>>(dispatchers.io) {

    override suspend fun doWork(params: Command.ChatCommand.GetMessagesByIds): List<Chat.Message> {
        return repo.getChatMessagesByIds(params)
    }
}