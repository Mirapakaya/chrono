package com.chronoio.chrono.di.main

import android.content.Context
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.domain.widgets.OsWidgetSpacesSync
import com.chronoio.chrono.feature_os_widgets.ui.OsSpacesListWidgetUpdater

/**
 * Decorator for [OsWidgetSpacesSync] that also triggers a widget refresh
 * after syncing spaces data to DataStore.
 */
class OsWidgetSpacesSyncWithUpdate(
    private val delegate: OsWidgetSpacesSync,
    private val context: Context
) : OsWidgetSpacesSync {

    override suspend fun sync(spaces: List<ObjectWrapper.SpaceView>) {
        // First, sync data to DataStore
        delegate.sync(spaces)
        // Then, trigger widget refresh so it picks up the new data
        OsSpacesListWidgetUpdater.updateAllWidgets(context)
    }
}
