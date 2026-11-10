package com.chronoio.chrono.domain.auth

import com.chronoio.chrono.domain.auth.interactor.SetupWallet
import com.chronoio.chrono.domain.auth.model.Wallet
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.core_models.CoroutineTestRule
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.common.DefaultCoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class SetupWalletTest {

    @get:Rule
    var rule = DefaultCoroutineTestRule()

    @Mock
    lateinit var repo: AuthRepository

    lateinit var dispatchers: AppCoroutineDispatchers

    lateinit var setupWallet: SetupWallet

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        dispatchers = AppCoroutineDispatchers(
            io = rule.dispatcher,
            computation = rule.dispatcher,
            main = rule.dispatcher
        )
        setupWallet = SetupWallet(repo, dispatchers)
    }

    @Test
    fun `should successfully create wallet and successfully save mnemonic`() = runBlocking {

        val path = "path"
        val mnemonic = "mnemonic"

        val wallet = Wallet(
            mnemonic = mnemonic
        )

        val param = SetupWallet.Params(path)

        repo.stub {
            onBlocking { createWallet(path) } doReturn wallet
        }

        repo.stub {
            onBlocking { saveMnemonic(mnemonic) } doReturn Unit
        }

        setupWallet.run(param)

        verify(repo, times(1)).createWallet(path)
        verify(repo, times(1)).saveMnemonic(mnemonic)
        verifyNoMoreInteractions(repo)
    }
}