package com.chronoio.chrono.domain.error

sealed class Error {
    object BackwardCompatibility : Error()
    object NotFoundObject : Error()
}