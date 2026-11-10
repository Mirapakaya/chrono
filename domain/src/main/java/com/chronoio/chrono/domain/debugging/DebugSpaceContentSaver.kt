package com.chronoio.chrono.domain.debugging

import java.io.File

interface DebugSpaceContentSaver {
    fun save(content: String) : File
}