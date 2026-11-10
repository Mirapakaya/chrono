package com.chronoio.chrono.emojifier.data

import com.chronoio.chrono.domain.icon.DocumentEmojiIconProvider

class DefaultDocumentEmojiIconProvider : DocumentEmojiIconProvider {

    companion object {
        val DOCUMENT_SET = listOf(
            "🌳", "⛳", "🧬", "🎈", "🎓",
            "💡", "🎒", "🚀", "🤖", "📚",
            "🍎", "🏡", "🤝", "😍", "☕",
            "🔥", "💥", "✍", "⏳", "📌",
            "🖍", "🦉", "📮", "📄", "🖌",
            "🗳", "⏰", "🔑", "🎉", "🗃",
            "🔖", "🧠", "👁", "🎗", "🎲",
            "🏙", "🚲", "⚙", "🔶", "🌍",
            "🏕", "🎡", "🌵", "🚗", "🚂",
            "🖼", "⭐", "🥁", "🛴", "🛫",
            "🏔", "🏗", "🛠", "🔍", "🕹",
            "🛋", "🎁", "🧮", "🏜", "🌋",
            "🎇", "🍏", "💫", "🌿", "🦊",
            "🍁", "🐎", "🍋", "🏍", "⛵"
        )
    }

    override fun random(): String = DOCUMENT_SET.random()
}