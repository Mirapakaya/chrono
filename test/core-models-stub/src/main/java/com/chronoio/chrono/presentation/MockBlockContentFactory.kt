package com.chronoio.chrono.presentation

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Block.Content.Link
import com.chronoio.chrono.core_models.Key
import com.chronoio.chrono.test_utils.MockDataFactory

object MockBlockContentFactory {

    fun StubLinkContent(
        target: String = MockDataFactory.randomUuid(),
        type: Link.Type = Link.Type.PAGE,
        iconSize: Link.IconSize = Link.IconSize.SMALL,
        cardStyle: Link.CardStyle = Link.CardStyle.TEXT,
        description: Link.Description = Link.Description.NONE,
        relations: Set<Key> = emptySet(),
    ): Link = Link(
        target = target,
        type = type,
        iconSize = iconSize,
        cardStyle = cardStyle,
        description = description,
        relations = relations
    )

    fun StubTextContent(
        text: String = MockDataFactory.randomString(),
        style: Block.Content.Text.Style = Block.Content.Text.Style.P,
        marks: List<Block.Content.Text.Mark> = emptyList(),
        isChecked: Boolean? = null,
        color: String? = null,
        align: Block.Align? = null,
        iconEmoji: String? = null,
        iconImage: String? = null,
    ): Block.Content.Text = Block.Content.Text(
        text, style, marks, isChecked, color, align, iconEmoji, iconImage
    )

}