package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.Id

interface SpaceInviteResolver {
    fun parseContentId(link: String) : Id?
    fun parseFileKey(link: String) : Id?
    fun createInviteLink(contentId: String, encryptionKey: String) : String
}