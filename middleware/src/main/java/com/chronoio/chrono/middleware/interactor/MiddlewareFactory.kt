package com.chronoio.chrono.middleware.interactor

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.middleware.mappers.MBBookmark
import com.chronoio.chrono.middleware.mappers.MBDiv
import com.chronoio.chrono.middleware.mappers.MBDivStyle
import com.chronoio.chrono.middleware.mappers.MBFile
import com.chronoio.chrono.middleware.mappers.MBLink
import com.chronoio.chrono.middleware.mappers.MBMarks
import com.chronoio.chrono.middleware.mappers.MBRelation
import com.chronoio.chrono.middleware.mappers.MBTableOfContents
import com.chronoio.chrono.middleware.mappers.MBText
import com.chronoio.chrono.middleware.mappers.MBlock
import com.chronoio.chrono.middleware.mappers.MBookmarkState
import com.chronoio.chrono.middleware.mappers.toMiddlewareModel

class MiddlewareFactory {

    fun create(prototype: Block.Prototype): MBlock {
        return when (prototype) {
            is Block.Prototype.Bookmark.New -> {
                val bookmark = MBBookmark()
                MBlock(bookmark = bookmark)
            }
            is Block.Prototype.Bookmark.Existing -> {
                val bookmark = MBBookmark(
                    targetObjectId = prototype.target,
                    state = MBookmarkState.Done
                )
                MBlock(bookmark = bookmark)
            }
            is Block.Prototype.Text -> {
                val text = MBText(
                    style = prototype.style.toMiddlewareModel(),
                    text = prototype.text.orEmpty(),
                    marks = if (prototype.marks.isEmpty()) {
                        null
                    } else {
                        MBMarks(marks = prototype.marks.map { it.toMiddlewareModel() })
                    },
                    checked = prototype.checked ?: false,
                    color = prototype.color.orEmpty(),
                    iconEmoji = prototype.iconEmoji.orEmpty(),
                    iconImage = prototype.iconImage.orEmpty()
                )
                MBlock(
                    text = text,
                    backgroundColor = prototype.backgroundColor.orEmpty(),
                    align = prototype.align.toMiddlewareModel(),
                    fields = prototype.fields
                        ?.takeIf { it.map.isNotEmpty() }
                        ?.toMiddlewareModel()
                )
            }
            is Block.Prototype.DividerLine -> {
                val divider = MBDiv(style = MBDivStyle.Line)
                MBlock(div = divider)
            }
            is Block.Prototype.DividerDots -> {
                val divider = MBDiv(style = MBDivStyle.Dots)
                MBlock(div = divider)
            }
            is Block.Prototype.File -> {
                val file = MBFile(
                    state = prototype.state.toMiddlewareModel(),
                    type = prototype.type.toMiddlewareModel(),
                    targetObjectId = prototype.targetObjectId.orEmpty()
                )
                MBlock(file_ = file)
            }
            is Block.Prototype.Link -> {
                val link = MBLink(
                    targetBlockId = prototype.target,
                    cardStyle = prototype.cardStyle.toMiddlewareModel(),
                    iconSize = prototype.iconSize.toMiddlewareModel(),
                    description = prototype.description.toMiddlewareModel()
                )
                MBlock(link = link)
            }
            is Block.Prototype.Relation -> {
                val relation = MBRelation(
                    key = prototype.key
                )
                MBlock(relation = relation)
            }
            is Block.Prototype.TableOfContents -> {
                val toc = MBTableOfContents()
                MBlock(tableOfContents = toc)
            }
            else -> throw IllegalStateException("Unexpected prototype: $prototype")
        }
    }
}