package com.chronoio.chrono.domain.cover

import com.chronoio.chrono.core_models.Id


interface GradientCollectionProvider {
    fun provide(): List<Id>
}