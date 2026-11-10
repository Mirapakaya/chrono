package com.chronoio.chrono.domain.chats

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.chats.Chat
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetChatMessages @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): ResultInteractor<Command.ChatCommand.GetMessages, Command.ChatCommand.GetMessages.Response>(dispatchers.io) {


    override suspend fun doWork(params: Command.ChatCommand.GetMessages): Command.ChatCommand.GetMessages.Response {
        return repo.getChatMessages(params)
    }
}