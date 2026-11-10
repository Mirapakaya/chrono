package com.chronoio.chrono

interface CrashReporter {
    fun init()
    fun setUser(userId: String)
}