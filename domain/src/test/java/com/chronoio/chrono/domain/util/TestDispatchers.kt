package com.chronoio.chrono.domain.util

import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
val dispatchers = AppCoroutineDispatchers(
    io = StandardTestDispatcher(),
    main = StandardTestDispatcher(),
    computation = StandardTestDispatcher()
)