package com.chronoio.chrono.core_utils.ext

fun String?.orNull(): String? = this?.ifEmpty { null }