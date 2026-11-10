package com.chronoio.chrono.feature_vault.presentation

import com.chronoio.chrono.domain.base.fold
import com.chronoio.chrono.domain.spaces.ClearLastOpenedSpace
import com.chronoio.chrono.domain.workspace.SpaceManager
import javax.inject.Inject
import timber.log.Timber

interface ExitToVaultDelegate {

    suspend fun proceedWithClearingSpaceBeforeExitingToVault()

    class Default @Inject constructor(
        val spaceManager: SpaceManager,
        val clearLastOpenedSpace: ClearLastOpenedSpace
    ): ExitToVaultDelegate {
        override suspend fun proceedWithClearingSpaceBeforeExitingToVault() {
            spaceManager.clear()
            clearLastOpenedSpace.async(Unit).fold(
                onSuccess = {
                    Timber.d("Cleared last opened space before opening vault")
                },
                onFailure = {
                    Timber.e(it, "Error while clearing last opened space before opening vault")
                }
            )
        }
    }

}
