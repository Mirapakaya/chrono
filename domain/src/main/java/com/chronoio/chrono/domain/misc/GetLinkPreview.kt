package com.chronoio.chrono.domain.misc

import com.chronoio.chrono.core_models.LinkPreview
import com.chronoio.chrono.core_models.Url
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetLinkPreview @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Url, LinkPreview>(dispatchers.io) {
    override suspend fun doWork(params: Url): LinkPreview {
        return repo.getLinkPreview(url = params)
    }
}