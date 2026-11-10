package com.chronoio.chrono.domain.vault

import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.config.UserSettingsRepository
import javax.inject.Inject

/**
 * Marks the spaces introduction popup as shown device-wide.
 * This is a one-time flag per device, not per account.
 */
class SetSpacesIntroductionShown @Inject constructor(
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Unit) {
        settings.setHasShownSpacesIntroduction(true)
    }
}