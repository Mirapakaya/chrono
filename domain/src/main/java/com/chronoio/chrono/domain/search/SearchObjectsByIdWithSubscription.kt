package com.chronoio.chrono.domain.search

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.SearchResult
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.ResultatInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class SearchObjectsByIdWithSubscription(
    private val repo: BlockRepository
) : ResultatInteractor<SearchObjectsByIdWithSubscription.Params, SearchResult>() {

    override suspend fun execute(params: Params): SearchResult {
        return repo.searchObjectsByIdWithSubscription(
            space = params.space,
            subscription = params.subscription,
            ids = params.ids,
            keys = params.keys
        )
    }

    class Params(
        val space: SpaceId,
        val subscription: Id,
        val ids: List<Id>,
        val keys: List<String>
    )
}