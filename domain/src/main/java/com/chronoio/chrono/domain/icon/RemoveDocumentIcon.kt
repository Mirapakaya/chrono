package com.chronoio.chrono.domain.icon

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for remove icon from object.
 */
class RemoveDocumentIcon(
    private val repo: BlockRepository
) : RemoveIcon<Id>() {

    override suspend fun run(params: Id) = safe {
        repo.removeDocumentIcon(params)
    }
}