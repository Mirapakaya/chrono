package com.chronoio.chrono.persistence

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.chronoio.chrono.domain.network.NetworkModeProvider
import com.chronoio.chrono.persistence.db.AnytypeDatabase
import com.chronoio.chrono.persistence.networkmode.DefaultNetworkModeProvider
import com.chronoio.chrono.persistence.repo.DefaultAuthCache
import com.chronoio.chrono.test_utils.MockDataFactory
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [33])
class DefaultAuthCacheTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val database = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().context,
        AnytypeDatabase::class.java
    ).allowMainThreadQueries().build()

    private val encryptedPrefs = RuntimeEnvironment.getApplication().getSharedPreferences(
        "Encrypted prefs",
        Context.MODE_PRIVATE
    )

    private val defaultPrefs = RuntimeEnvironment.getApplication().getSharedPreferences(
        "Default prefs",
        Context.MODE_PRIVATE
    )

    lateinit var networkModeProvider: NetworkModeProvider

    @After
    fun after() {
        database.close()
    }

    @Test
    fun `should save mnemonic and clear mnemonic on logout`() = runTest {

        val givenMnemonic = MockDataFactory.randomString()

        val networkModeProvider = DefaultNetworkModeProvider(
            sharedPreferences = defaultPrefs
        )

        val cache = DefaultAuthCache(
            db = database,
            encryptedPrefs = encryptedPrefs,
            defaultPrefs = defaultPrefs,
            networkModeProvider = networkModeProvider
        )

        cache.saveMnemonic(mnemonic = givenMnemonic)

        assertTrue { encryptedPrefs.contains(DefaultAuthCache.MNEMONIC_KEY) }

        assertEquals(
            expected = givenMnemonic,
            actual = cache.getMnemonic()
        )

        cache.logout()

        assertFalse { encryptedPrefs.contains(DefaultAuthCache.MNEMONIC_KEY) }
        assertFalse { defaultPrefs.contains(DefaultAuthCache.MNEMONIC_KEY) }
    }

}