package com.chronoio.chrono.domain.misc

import java.util.Locale

interface LocaleProvider {
    fun language() : String
    fun locale() : Locale
}