package com.chronoio.chrono.clipboard

import com.chronoio.chrono.data.auth.other.ClipboardUriMatcher

class AnytypeUriMatcher : ClipboardUriMatcher {
    override fun isAnytypeUri(uri: String): Boolean {
        return uri == BuildConfig.ANYTYPE_CLIPBOARD_URI
    }
}