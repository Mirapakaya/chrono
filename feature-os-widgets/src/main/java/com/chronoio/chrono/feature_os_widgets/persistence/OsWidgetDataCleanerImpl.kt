package com.chronoio.chrono.feature_os_widgets.persistence

import android.content.Context
import com.chronoio.chrono.domain.widgets.OsWidgetDataCleaner

class OsWidgetDataCleanerImpl(
    private val context: Context
) : OsWidgetDataCleaner {

    private val dataStore by lazy { OsWidgetsDataStore(context) }
    private val iconCache by lazy { OsWidgetIconCache(context) }

    override suspend fun clearAll() {
        dataStore.clear()
        iconCache.clearAll()
    }
}
