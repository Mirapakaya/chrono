package com.chronoio.chrono.domain.search

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.GlobalSearchHistory
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

class SearchWithMeta @Inject constructor(
    private val repo: BlockRepository,
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SearchWithMeta.Params, List<Command.SearchWithMeta.Result>>(dispatchers.io) {
    override suspend fun doWork(params: SearchWithMeta.Params): List<Command.SearchWithMeta.Result> {
        return repo.searchObjectWithMeta(command = params.command).also {
            if (params.saveSearch) saveSearch(params)
        }
    }

    private suspend fun saveSearch(params: Params) {
        val search = GlobalSearchHistory(
            query = params.command.query,
            relatedObject = params.relatedObjectId
        )
        settings.setGlobalSearchHistory(globalSearchHistory = search, space = params.command.space)
    }

    data class Params(
        val command: Command.SearchWithMeta,
        val relatedObjectId: Id?,
        val saveSearch: Boolean = false
    )
}