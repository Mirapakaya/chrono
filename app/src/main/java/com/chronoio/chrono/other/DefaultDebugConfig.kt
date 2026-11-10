package com.chronoio.chrono.other

import com.chronoio.chrono.BuildConfig
import com.chronoio.chrono.domain.debugging.DebugConfig
import javax.inject.Inject

class DefaultDebugConfig @Inject constructor(): DebugConfig {
    override val traceSubscriptions: Boolean get() = BuildConfig.DEBUG
    override val setTimeouts: Boolean get() = BuildConfig.DEBUG
}