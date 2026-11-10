package com.chronoio.chrono.domain.cover

import com.chronoio.chrono.domain.base.BaseUseCase

class GetCoverImageCollection(private val provider: CoverCollectionProvider) : BaseUseCase<List<CoverImage>, Unit>() {
    override suspend fun run(params: Unit) = safe {
        provider.provide()
    }
}