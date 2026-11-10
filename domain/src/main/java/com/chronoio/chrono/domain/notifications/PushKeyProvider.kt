package com.chronoio.chrono.domain.notifications

import com.chronoio.chrono.presentation.notifications.PushKey

interface PushKeyProvider {
    fun start()
    fun stop()
    fun getPushKey(): Map<String, PushKey>
}