package com.chronoio.chrono.domain.misc

interface Reducer<STATE, EVENT> {
    fun reduce(state: STATE, event: EVENT): STATE
}