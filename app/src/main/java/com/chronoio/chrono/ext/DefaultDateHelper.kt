package com.chronoio.chrono.ext

import com.chronoio.chrono.core_utils.date.isToday
import com.chronoio.chrono.core_utils.date.isTomorrow
import com.chronoio.chrono.domain.account.DateHelper

class DefaultDateHelper : DateHelper {
    override fun isToday(millis: Long): Boolean {
        return millis.isToday()
    }

    override fun isTomorrow(millis: Long): Boolean {
        return millis.isTomorrow()
    }
}