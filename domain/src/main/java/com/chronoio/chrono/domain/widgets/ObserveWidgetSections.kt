package com.chronoio.chrono.domain.widgets

import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.FlowInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.core_models.WidgetSections
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/**
 * Use-case for observing widget sections configuration for a space.
 * Returns the order and visibility settings for home screen sections.
 */
class ObserveWidgetSections @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : FlowInteractor<ObserveWidgetSections.Params, WidgetSections>(dispatchers.io) {

    override fun build(): Flow<WidgetSections> {
        throw UnsupportedOperationException("Use build(params) instead")
    }

    override fun build(params: Params): Flow<WidgetSections> {
        return userSettingsRepository
            .observeWidgetSections(params.spaceId)
            .catch { emit(WidgetSections.default()) }
    }

    data class Params(val spaceId: SpaceId)
}
