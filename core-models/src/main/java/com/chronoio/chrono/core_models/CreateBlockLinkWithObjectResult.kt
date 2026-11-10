package com.chronoio.chrono.core_models

data class CreateBlockLinkWithObjectResult(
    val blockId: Id,
    val objectId: Id,
    val event: Payload,
    val details: Struct
)

