package com.chronoio.chrono.device

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.cover.GradientCollectionProvider
import com.chronoio.chrono.presentation.editor.cover.CoverGradient

object DefaultGradientCollectionProvider : GradientCollectionProvider {
    override fun provide(): List<Id> = CoverGradient.default
}