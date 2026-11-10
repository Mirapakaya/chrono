package com.chronoio.chrono.domain.objects

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Struct
import com.chronoio.chrono.core_models.Url
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for creating bookmark object from url.
 */
class CreateBookmarkObject @Inject constructor(
    private val repo: BlockRepository
) : BaseUseCase<Id, CreateBookmarkObject.Params>() {

    override suspend fun run(params: Params) = safe {
        repo.createBookmarkObject(
            space = params.space,
            url = params.url,
            details = params.details,
            createdInContext = params.createdInContext
        )
    }

    data class Params(
        val space: Id,
        val url: Url,
        val details: Struct = emptyMap(),
        val createdInContext: Id? = null
    )
}
