package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.Interactor
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.launch.RemainingSpacesPreloader
import com.chronoio.chrono.domain.workspace.SpaceManager
import javax.inject.Inject

/**
 * Use case for logging out.
 */
class Logout @Inject constructor(
    private val repo: AuthRepository,
    private val config: ConfigStorage,
    private val user: UserSettingsRepository,
    private val spaceManager: SpaceManager,
    private val awaitAccountStartManager: AwaitAccountStartManager,
    private val remainingSpacesPreloader: RemainingSpacesPreloader,
    private val preferredSpaceIdHolder: PreferredSpaceIdHolder,
    dispatchers: AppCoroutineDispatchers,
) : Interactor<Logout.Params>(context = dispatchers.io) {

    override suspend fun run(params: Params) {
        repo.logout(clearLocalRepositoryData = params.clearLocalRepositoryData)
        user.clear()
        config.clear()
        spaceManager.clear()
        remainingSpacesPreloader.reset()
        // Drop any preferred space id set but not consumed this process (e.g. a
        // cold-start deeplink that lost the LaunchAccount race), so it can't leak
        // into the next login's LaunchAccount.
        preferredSpaceIdHolder.clear()
        awaitAccountStartManager.setState(AwaitAccountStartManager.State.Stopped)
    }

    class Params(
        val clearLocalRepositoryData: Boolean = false
    )
}