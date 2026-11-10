package com.chronoio.chrono.data.auth.repo.config

import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.Gateway
import com.chronoio.chrono.domain.debugging.Logger

class GatewayProvider(
    private val configStorage: ConfigStorage,
    private val logger: Logger
) : Gateway {
    override fun provide(): String {
        val gateway = configStorage.getOrNull()?.gateway
        if (gateway == null) {
            logger.logException(IllegalStateException("Gateway not found"))
        }
        return gateway.orEmpty()
    }

}