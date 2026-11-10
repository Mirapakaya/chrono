package com.chronoio.chrono.presentation.editor.cover

import com.chronoio.chrono.core_models.Hash
import com.chronoio.chrono.core_models.Id


interface CoverImageHashProvider {
    fun provide(id: Id): Hash?
}