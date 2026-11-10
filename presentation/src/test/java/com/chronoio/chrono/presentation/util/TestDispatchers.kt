package com.chronoio.chrono.presentation.util

import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
val dispatchers = AppCoroutineDispatchers(
    io = UnconfinedTestDispatcher(),
    main = UnconfinedTestDispatcher(),
    computation = UnconfinedTestDispatcher()
)
