package com.chronoio.chrono.data.auth.other

import com.chronoio.chrono.domain.clipboard.Clipboard

class ClipboardDataUriMatcher(
    private val matcher: ClipboardUriMatcher
) : Clipboard.UriMatcher {

    override fun isAnytypeUri(
        uri: String
    ): Boolean = matcher.isAnytypeUri(uri)
}