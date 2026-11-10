package com.chronoio.chrono.domain.chats

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class SearchChatMessages @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Command.ChatCommand.SearchMessages, Command.ChatCommand.SearchMessages.Response>(dispatchers.io) {
    override suspend fun doWork(params: Command.ChatCommand.SearchMessages): Command.ChatCommand.SearchMessages.Response {
        return repo.searchChatMessages(params)
    }
}
