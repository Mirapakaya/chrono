package com.chronoio.chrono.domain.auth

import com.chronoio.chrono.core_models.CoroutineTestRule
import com.chronoio.chrono.core_models.StubConfig
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.interactor.ResumeAccount
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.device.PathProvider
import com.chronoio.chrono.domain.platform.InitialParamsProvider
import com.chronoio.chrono.domain.workspace.SpaceManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ResumeAccountTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var rule = CoroutineTestRule()

    @Mock
    lateinit var repo: AuthRepository

    @Mock
    lateinit var configStorage: ConfigStorage

    @Mock
    lateinit var spaceManager: SpaceManager

    @Mock
    lateinit var userSettingsRepository: UserSettingsRepository

    @Mock
    lateinit var pathProvider: PathProvider

    @Mock
    lateinit var initialParamsProvider: InitialParamsProvider

    @Mock
    lateinit var awaitAccountStartManager: AwaitAccountStartManager

    lateinit var resumeAccount: ResumeAccount

    private val config = StubConfig()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        resumeAccount = ResumeAccount(
            repository = repo,
            configStorage = configStorage,
            pathProvider = pathProvider,
            initialParamsProvider = initialParamsProvider,
            awaitAccountStartManager = awaitAccountStartManager,
            spaceManager = spaceManager,
            settings = userSettingsRepository
        )
    }
}