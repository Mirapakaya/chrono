package com.chronoio.chrono.device

import android.content.Context
import com.chronoio.chrono.domain.device.PathProvider

class DefaultPathProvider(
    private val context: Context
) : PathProvider {
    override fun providePath(): String = context.filesDir.path
    override fun cachePath(): String = context.cacheDir.path
}