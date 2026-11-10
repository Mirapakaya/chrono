package com.chronoio.chrono.other

import com.chronoio.chrono.domain.debugging.Logger
import javax.inject.Inject
import timber.log.Timber

class BasicLogger @Inject constructor() : Logger {
    override fun logWarning(msg: String) {
        Timber.w(msg)
    }

    override fun logException(e: Throwable) {
        Timber.e(e)
    }

    override fun logException(e: Throwable, msg: String) {
        Timber.e(e, msg)
    }

    override fun logInfo(msg: String) {
        Timber.i(msg)
    }
}