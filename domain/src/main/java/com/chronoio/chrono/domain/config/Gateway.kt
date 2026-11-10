package com.chronoio.chrono.domain.config

interface Gateway {
    fun provide(): String
}