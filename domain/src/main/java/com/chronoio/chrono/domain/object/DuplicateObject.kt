package com.chronoio.chrono.domain.`object`

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository

class DuplicateObject(
    private val repo: BlockRepository
) : BaseUseCase<Id, Id>() {

    override suspend fun run(params: Id) = safe {
        repo.duplicateObject(params)
    }
}