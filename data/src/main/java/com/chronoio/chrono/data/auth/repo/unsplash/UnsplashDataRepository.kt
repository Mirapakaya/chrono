package com.chronoio.chrono.data.auth.repo.unsplash

import com.chronoio.chrono.core_models.Hash
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.UnsplashImage
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.unsplash.UnsplashRepository

class UnsplashDataRepository(
    private val remote: UnsplashRemote
) : UnsplashRepository {
    override fun search(
        query: String,
        limit: Int
    ): List<UnsplashImage> = remote.search(
        query = query,
        limit = limit
    )
    override fun download(
        id: Id,
        space: SpaceId,
        createdInContext: Id?,
        createdInContextRef: String?
    ): Hash = remote.download(
        id = id,
        space = space,
        createdInContext = createdInContext,
        createdInContextRef = createdInContextRef
    )
}