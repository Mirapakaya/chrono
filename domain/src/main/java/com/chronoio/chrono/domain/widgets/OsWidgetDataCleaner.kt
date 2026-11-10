package com.chronoio.chrono.domain.widgets

interface OsWidgetDataCleaner {
    suspend fun clearAll()

    object NoOp : OsWidgetDataCleaner {
        override suspend fun clearAll() = Unit
    }
}
