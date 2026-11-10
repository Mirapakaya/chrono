package com.chronoio.chrono.domain.device

interface PathProvider {
    fun providePath(): String
    fun cachePath(): String
}