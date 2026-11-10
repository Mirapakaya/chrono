package com.chronoio.chrono.domain.chats

import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.FlowInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll

class ObserveRecentlyUsedChatReactions @Inject constructor(
    private val auth: AuthRepository,
    private val repo: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : FlowInteractor<Unit, List<String>>(dispatchers.io) {

    override fun build(): Flow<List<String>> = flow {
        val acc = auth.getCurrentAccount()
        emitAll(repo.observeRecentlyUsedChatReactions(acc))
    }

    override fun build(params: Unit): Flow<List<String>> = flow {
        val acc = auth.getCurrentAccount()
        emitAll(repo.observeRecentlyUsedChatReactions(acc))
    }
}