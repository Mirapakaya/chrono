package com.chronoio.chrono.domain.clipboard

import com.chronoio.chrono.core_models.*
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository

/**
 * Use-case for pasting to Anytype clipboard.
 */
class Paste(
    private val repo: BlockRepository,
    private val clipboard: Clipboard,
    private val matcher: Clipboard.UriMatcher
) : BaseUseCase<Response.Clipboard.Paste, Paste.Params>() {

    override suspend fun run(params: Params) = safe {
        val clip = clipboard.clip()

        if (clip != null) {

            val uri = clip.uri

            val blocks = if (uri != null && matcher.isAnytypeUri(uri))
                clipboard.blocks()
            else
                emptyList()

            repo.paste(
                command = Command.Paste(
                    context = params.context,
                    focus = params.focus,
                    selected = params.selected,
                    range = params.range,
                    text = clip.text,
                    html = clip.html,
                    blocks = blocks,
                    isPartOfBlock = params.isPartOfBlock
                )
            )
        } else {
            throw IllegalStateException("Empty clip!")
        }
    }

    /**
     * Params for pasting to Anytype clipboard
     * @property context id of the context
     * @property focus id of the focused/target block
     * @property range selected text range
     */
    data class Params(
        val context: Id,
        val focus: Id,
        val range: IntRange,
        val selected: List<Id>,
        val isPartOfBlock: Boolean? = null
    )

    companion object {
        val DEFAULT_RANGE = IntRange(0, 0)
    }
}