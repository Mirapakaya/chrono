package com.chronoio.chrono.middleware.converters

import anytype.clipboard.Clipboard
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.data.auth.mapper.Serializer
import com.chronoio.chrono.middleware.mappers.toCoreModels
import com.chronoio.chrono.middleware.mappers.toMiddlewareModel

class ClipboardSerializer : Serializer {

    override fun serialize(blocks: List<Block>): ByteArray {
        val models = blocks.map { it.toMiddlewareModel() }
        val clipboard = Clipboard(blocks = models)
        return Clipboard.ADAPTER.encode(clipboard)
    }

    override fun deserialize(blob: ByteArray): List<Block> {
        return Clipboard.ADAPTER.decode(blob).blocks.toCoreModels()
    }
}