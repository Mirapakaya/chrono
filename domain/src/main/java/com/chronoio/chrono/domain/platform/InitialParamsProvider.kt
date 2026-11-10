package com.chronoio.chrono.domain.platform

import com.chronoio.chrono.core_models.Command

interface InitialParamsProvider {
    fun getVersion(): String
    fun getPlatform(): String
    val workDir: String
    val defaultLogLevel: String
    fun toCommand(): Command.SetInitialParams
}