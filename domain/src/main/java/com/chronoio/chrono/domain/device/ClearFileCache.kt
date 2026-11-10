package com.chronoio.chrono.domain.device

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Interactor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class ClearFileCache(
    private val repo: BlockRepository
): Interactor<BaseUseCase.None>() {
    override suspend fun run(params: BaseUseCase.None) { repo.clearFileCache() }
}