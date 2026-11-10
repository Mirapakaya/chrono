package com.chronoio.chrono.domain.base

import com.chronoio.chrono.domain.error.Error

@Deprecated(
    message = "Result is deprecated",
    replaceWith = ReplaceWith("Resultat", "com.chronoio.chrono.domain.base")
)
sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(val error: Error) : Result<T>()
}