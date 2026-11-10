package com.chronoio.chrono.data.auth.repo.unsplash

import com.chronoio.chrono.core_models.Hash
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.UnsplashImage
import com.chronoio.chrono.core_models.primitives.SpaceId

interface UnsplashRemote {
    fun search(query: String, limit: Int) : List<UnsplashImage>
    fun download(
        space: SpaceId,
        id: Id,
        createdInContext: Id? = null,
        createdInContextRef: String? = null
    ): Hash
}