package com.chronoio.chrono.domain.auth

import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.interactor.Logout
import com.chronoio.chrono.domain.auth.interactor.PreloadRemainingSpaces
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.common.DefaultCoroutineTestRule
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.launch.PreferredSpaceIdHolder
import com.chronoio.chrono.domain.launch.RemainingSpacesPreloader
import com.chronoio.chrono.domain.workspace.SpaceManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class LogoutTest {

    @get:Rule
    val rule = DefaultCoroutineTestRule()

    @Mock lateinit var repo: AuthRepository
    @Mock lateinit var config: ConfigStorage
    @Mock lateinit var user: UserSettingsRepository
    @Mock lateinit var spaceManager: SpaceManager
    @Mock lateinit var awaitAccountStartManager: AwaitAccountStartManager
    @Mock lateinit var preferredSpaceIdHolder: PreferredSpaceIdHolder

    private lateinit var logout: Logout

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val dispatchers = AppCoroutineDispatchers(
            io = rule.dispatcher,
            computation = rule.dispatcher,
            main = rule.dispatcher
        )
        logout = Logout(
            repo = repo,
            config = config,
            user = user,
            spaceManager = spaceManager,
            awaitAccountStartManager = awaitAccountStartManager,
            remainingSpacesPreloader = RemainingSpacesPreloader(
                PreloadRemainingSpaces(dispatchers, repo)
            ),
            preferredSpaceIdHolder = preferredSpaceIdHolder,
            dispatchers = dispatchers
        )
    }

    @Test
    fun `clears preferred space id holder on logout`() = runTest {
        logout(Logout.Params()).collect {}
        verify(preferredSpaceIdHolder).clear()
    }
}
