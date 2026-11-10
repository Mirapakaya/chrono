package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.clipboard.Clipboard
import javax.inject.Inject

/**
 * Use case for copying invite link to clipboard
 */
class CopyInviteLinkToClipboard @Inject constructor(
    private val clipboard: Clipboard
) : BaseUseCase<Unit, CopyInviteLinkToClipboard.Params>() {

    override suspend fun run(params: Params) = safe {
        clipboard.put(
            text = params.link,
            html = null,
            blocks = emptyList<Block>()
        )
    }

    data class Params(
        val link: String
    )
}