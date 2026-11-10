package com.chronoio.chrono.util

import android.webkit.URLUtil
import com.chronoio.chrono.presentation.util.UrlHelper

class UrlHelperImpl : UrlHelper {
    override fun isValidUrl(url: String): Boolean {
        return URLUtil.isValidUrl(url)
    }
}
