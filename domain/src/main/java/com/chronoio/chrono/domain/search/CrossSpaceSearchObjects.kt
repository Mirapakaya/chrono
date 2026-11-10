package com.chronoio.chrono.domain.search

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * One-shot object search across all user spaces (vault-wide).
 * A single-space scope is expressed via a SPACE_ID filter in the command.
 * The result's allStoresLoaded == false means a partial view (per-space
 * stores still warming up) — render it as-is, do not retry in a loop.
 */
class CrossSpaceSearchObjects @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Command.CrossSpaceSearch, Command.CrossSpaceSearch.Result>(dispatchers.io) {
    override suspend fun doWork(params: Command.CrossSpaceSearch): Command.CrossSpaceSearch.Result {
        return repo.crossSpaceSearch(command = params)
    }
}
