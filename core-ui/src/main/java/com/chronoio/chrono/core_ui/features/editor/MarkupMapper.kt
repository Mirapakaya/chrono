package com.chronoio.chrono.core_ui.features.editor

import android.text.Editable
import com.chronoio.chrono.core_models.Block.Content.Text.Mark
import com.chronoio.chrono.core_ui.common.Span
import com.chronoio.chrono.core_ui.common.Underline
import com.chronoio.chrono.core_ui.widgets.text.MentionSpan
import com.chronoio.chrono.presentation.editor.editor.Markup

fun Editable.marks(): List<Markup.Mark> = getSpans(0, length, Span::class.java).mapNotNull { span ->
    when (span) {
        is Span.Strikethrough -> Markup.Mark.Strikethrough(
            from = getSpanStart(span),
            to = getSpanEnd(span)
        )
        is Span.TextColor -> Markup.Mark.TextColor(
            from = getSpanStart(span),
            to = getSpanEnd(span),
            color = span.value
        )
        is Span.Highlight -> Markup.Mark.BackgroundColor(
            from = getSpanStart(span),
            to = getSpanEnd(span),
            background = span.value
        )
        is Span.Italic -> Markup.Mark.Italic(
            from = getSpanStart(span),
            to = getSpanEnd(span)
        )
        is Underline -> Markup.Mark.Underline(
            from = getSpanStart(span),
            to = getSpanEnd(span)
        )
        is Span.Bold -> Markup.Mark.Bold(
            from = getSpanStart(span),
            to = getSpanEnd(span)
        )
        is Span.Keyboard -> Markup.Mark.Keyboard(
            from = getSpanStart(span),
            to = getSpanEnd(span)
        )
        is Span.Url -> Markup.Mark.Link(
            from = getSpanStart(span),
            to = getSpanEnd(span),
            param = span.url
        )
        is MentionSpan -> Markup.Mark.Mention.Base(
            from = getSpanStart(span),
            to = getSpanEnd(span),
            param = span.param.orEmpty(),
            isArchived = false
        )
        is Span.ObjectLink -> Markup.Mark.Object(
            from = getSpanStart(span),
            to = getSpanEnd(span),
            param = span.link.orEmpty(),
            isArchived = false
        )
        is Span.Emoji -> Markup.Mark.Emoji(
            from = getSpanStart(span),
            to = getSpanEnd(span),
            param = span.emojiText
        )
        else -> null
    }
}