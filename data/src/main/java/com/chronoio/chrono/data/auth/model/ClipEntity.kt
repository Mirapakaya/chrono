package com.chronoio.chrono.data.auth.model

import com.chronoio.chrono.domain.clipboard.Clip

/**
 * @see Clip
 */
class ClipEntity(
    override val text: String,
    override val html: String?,
    override val uri: String?
) : Clip