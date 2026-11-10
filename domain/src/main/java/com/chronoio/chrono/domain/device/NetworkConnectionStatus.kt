package com.chronoio.chrono.domain.device

import com.chronoio.chrono.core_models.DeviceNetworkType

interface NetworkConnectionStatus {
    fun start()
    fun stop()
    fun getCurrentNetworkType(): DeviceNetworkType
}