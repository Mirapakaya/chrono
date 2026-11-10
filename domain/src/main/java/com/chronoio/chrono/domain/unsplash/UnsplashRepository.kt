package com.chronoio.chrono.domain.unsplash

import com.chronoio.chrono.core_models.Hash
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.UnsplashImage
import com.chronoio.chrono.core_models.primitives.SpaceId

interface UnsplashRepository {
    fun search(query: String, limit: Int) : List<UnsplashImage>
    fun download(
        id: Id,
        space: SpaceId,
        createdInContext: Id? = null,
        createdInContextRef: String? = null
    ): Hash
}