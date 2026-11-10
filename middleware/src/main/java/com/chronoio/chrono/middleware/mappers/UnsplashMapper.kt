package com.chronoio.chrono.middleware.mappers

import anytype.Rpc.Unsplash.Search.Response.Picture
import com.chronoio.chrono.core_models.UnsplashImage

fun Picture.core() : UnsplashImage = UnsplashImage(
    id = id,
    url = url,
    artist = UnsplashImage.Artist(
        name = artist,
        url = artistUrl
    )
)