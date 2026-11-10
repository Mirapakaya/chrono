package com.chronoio.chrono.domain.auth

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.NetworkMode
import com.chronoio.chrono.core_models.NetworkModeConfig
import com.chronoio.chrono.core_models.StubAccountSetup
import com.chronoio.chrono.domain.account.AwaitAccountStartManager
import com.chronoio.chrono.domain.auth.interactor.CreateAccount
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.common.DefaultCoroutineTestRule
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.platform.InitialParamsProvider
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.test_utils.MockDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class CreateAccountTest {

    @get:Rule
    val rule = DefaultCoroutineTestRule()

    @Mock
    lateinit var repo: AuthRepository

    @Mock
    lateinit var configStorage: ConfigStorage

    lateinit var dispatchers: AppCoroutineDispatchers

    @Mock
    lateinit var initialParamsProvider: InitialParamsProvider

    @Mock
    lateinit var awaitAccountStartManager: AwaitAccountStartManager

    @Mock
    lateinit var spaceManager: SpaceManager

    private lateinit var createAccount: CreateAccount


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        dispatchers = AppCoroutineDispatchers(
            io = rule.dispatcher,
            computation = rule.dispatcher,
            main = rule.dispatcher
        )
        createAccount = CreateAccount(
            repository = repo,
            configStorage = configStorage,
            initialParamsProvider = initialParamsProvider,
            dispatcher = dispatchers,
            awaitAccountStartManager = awaitAccountStartManager,
            spaceManager = spaceManager
        )
    }

    @Test
    fun `should create account and save it and set as current user account and save config in storage and set default space`() =
        runTest {
            val name = MockDataFactory.randomString()
            val path = null
            val icon = 1
            val setup = StubAccountSetup()
            val param = CreateAccount.Params(
                name = name,
                avatarPath = path,
                iconGradientValue = icon
            )

            repo.stub {
                onBlocking {
                    getNetworkMode()
                } doReturn NetworkModeConfig(
                    networkMode = NetworkMode.DEFAULT
                )
            }

            repo.stub {
                val command = Command.AccountCreate(
                    name = name,
                    avatarPath = path,
                    icon = icon,
                    networkMode = NetworkMode.DEFAULT
                )
                onBlocking { createAccount(command) } doReturn setup
            }

            val version = MockDataFactory.randomString()
            val platform = MockDataFactory.randomString()

            stubMetricsProvider(version, platform)

            createAccount.run(param)

            val command = Command.AccountCreate(
                name = name,
                avatarPath = path,
                icon = icon,
                networkMode = NetworkMode.DEFAULT
            )
            verify(repo, times(1)).getNetworkMode()
            verify(repo, times(1)).createAccount(command)
            verify(repo, times(1)).saveAccount(setup.account)
            verify(repo, times(1)).setCurrentAccount(setup.account.id)
            verify(configStorage, times(1)).set(setup.config, setup.account.id)
            verify(spaceManager, times(1)).set(setup.config.space)
            verify(awaitAccountStartManager, times(1)).setState(AwaitAccountStartManager.State.Started)
        }

    private fun stubMetricsProvider(version: String, platform: String) {
        initialParamsProvider.stub {
            onBlocking {
                getVersion()
            } doReturn version
            onBlocking {
                getPlatform()
            } doReturn platform
        }
    }
}