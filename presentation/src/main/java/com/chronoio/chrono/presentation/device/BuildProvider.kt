package com.chronoio.chrono.presentation.device

import android.os.Build
import com.chronoio.chrono.presentation.BuildConfig

interface BuildProvider {
    fun getManufacturer(): String
    fun getModel(): String
    fun isDebug(): Boolean
}

class DefaultBuildProvider : BuildProvider {
    override fun getManufacturer(): String {
        return Build.MANUFACTURER
    }

    override fun getModel(): String {
        return Build.MODEL
    }

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}
