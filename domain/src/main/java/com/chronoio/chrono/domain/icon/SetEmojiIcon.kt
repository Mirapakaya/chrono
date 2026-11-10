package com.chronoio.chrono.domain.icon

import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.domain.base.BaseUseCase

abstract class SetEmojiIcon<T> :
    BaseUseCase<Payload, SetEmojiIcon.Params<T>>() {

    /**
     * @property emoji emoji's unicode
     * @property target i.e. object or text block
     */
    data class Params<T>(
        val target: T,
        val emoji: String
    )
}