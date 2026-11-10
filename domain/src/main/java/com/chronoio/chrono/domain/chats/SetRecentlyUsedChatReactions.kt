package com.chronoio.chrono.domain.chats

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class SetRecentlyUsedChatReactions @Inject constructor(
    private val auth: AuthRepository,
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Set<String>, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Set<String>) {
        val acc = auth.getCurrentAccount()
        repo.setRecentlyUsedChatReactions(
            account = acc,
            emojis = params
        )
    }
}