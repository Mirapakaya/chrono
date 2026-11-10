package com.chronoio.chrono.domain.icon

import com.chronoio.chrono.core_models.Hash
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.BaseUseCase

abstract class SetImageIcon<T> : BaseUseCase<Pair<Payload, Hash>, SetImageIcon.Params<T>>() {
    /**
     * Params for for setting document's image icon
     * @property path image path in file system
     * @property target i.e. id of the context or text blockId
     */
    data class Params<T>(
        val target: T,
        val path: String,
        val spaceId: SpaceId
    )
}