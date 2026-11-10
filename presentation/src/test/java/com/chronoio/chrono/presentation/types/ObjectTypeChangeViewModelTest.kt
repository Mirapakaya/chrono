package com.chronoio.chrono.presentation.types

import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.presentation.util.DefaultCoroutineTestRule
import com.chronoio.chrono.test_utils.MockDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock

@ExperimentalCoroutinesApi
class ObjectTypeChangeViewModelTest {

    @Mock
    lateinit var blockRepository: BlockRepository

    @Mock
    lateinit var userSettingsRepository: UserSettingsRepository

    @get:Rule
    val coroutineTestRule = DefaultCoroutineTestRule()

    private lateinit var getDefaultObjectType: GetDefaultObjectType

    val spaceId = MockDataFactory.randomUuid()

    @Mock
    lateinit var spaceViews: SpaceViewSubscriptionContainer

    private val dispatchers = AppCoroutineDispatchers(
        io = coroutineTestRule.dispatcher,
        main = coroutineTestRule.dispatcher,
        computation = coroutineTestRule.dispatcher
    )

    @Before
    fun before() {
    }
}