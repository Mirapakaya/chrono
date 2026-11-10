package com.chronoio.chrono.core_models

data class CreateObjectResult(
    val id: Id,
    val event: Payload,
    val details: Struct
)
