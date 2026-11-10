package com.chronoio.chrono.data.auth.mapper

import com.chronoio.chrono.core_models.Block

interface Serializer {
    fun serialize(blocks: List<Block>): ByteArray
    fun deserialize(blob: ByteArray): List<Block>
}