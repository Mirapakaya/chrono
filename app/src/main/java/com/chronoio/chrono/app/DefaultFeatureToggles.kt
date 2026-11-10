package com.chronoio.chrono.app

import com.chronoio.chrono.BuildConfig
import com.chronoio.chrono.core_utils.tools.FeatureToggles
import com.chronoio.chrono.presentation.device.BuildProvider
import javax.inject.Inject

class DefaultFeatureToggles @Inject constructor(
    buildProvider: BuildProvider
) : FeatureToggles {

    override val isLogFromGoProcess =
        BuildConfig.LOG_FROM_MW_LIBRARY && buildProvider.isDebug()

    override val isLogMiddlewareInteraction = BuildConfig.LOG_MW_INTERACTION && buildProvider.isDebug()

    override val isLogEditorViewModelEvents =
        BuildConfig.LOG_EDITOR_VIEWMODEL_EVENTS && buildProvider.isDebug()

    override val isLogEditorControlPanelMachine =
        BuildConfig.LOG_EDITOR_CONTROL_PANEL && buildProvider.isDebug()

    override val isConciseLogging: Boolean = true
}