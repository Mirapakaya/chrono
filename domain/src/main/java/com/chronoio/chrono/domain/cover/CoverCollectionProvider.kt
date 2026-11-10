package com.chronoio.chrono.domain.cover

interface CoverCollectionProvider {
    fun provide(): List<CoverImage>
}