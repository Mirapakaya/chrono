package com.chronoio.chrono.domain.device

interface DeviceTokenStoringService {
    fun saveToken(token: String)
    fun start()
    fun stop()
    suspend fun getToken(): String?
}