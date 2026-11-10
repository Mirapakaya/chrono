package com.chronoio.chrono

import android.os.Build
import com.chronoio.chrono.other.DefaultSpaceInviteResolver
import kotlin.test.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class DefaultSpaceInviteResolverTest {

    @Test
    fun `should parse file key and content id from deeplink`() {

        val fileKeyValue = "ae62abf2a19e"
        val cidValue = "75538c6c50351eca"

        val link =  "invite.chrono.app/${cidValue}#${fileKeyValue}"

        assertEquals(
            expected = cidValue,
            actual = DefaultSpaceInviteResolver.parseContentId(link)
        )

        assertEquals(
            expected = fileKeyValue,
            actual = DefaultSpaceInviteResolver.parseFileKey(link)
        )
    }

    @Test
    fun `should parse file key and content id from deeplink with https`() {

        val fileKeyValue = "ae62abf2a19e"
        val cidValue = "75538c6c50351eca"

        val link =  "https://invite.chrono.app/${cidValue}#${fileKeyValue}"

        assertEquals(
            expected = cidValue,
            actual = DefaultSpaceInviteResolver.parseContentId(link)
        )

        assertEquals(
            expected = fileKeyValue,
            actual = DefaultSpaceInviteResolver.parseFileKey(link)
        )
    }

    @Test
    fun `should parse file key and content id from deeplink with http`() {

        val fileKeyValue = "ae62abf2a19e"
        val cidValue = "75538c6c50351eca"

        val link =  "http://invite.chrono.app/${cidValue}#${fileKeyValue}"

        assertEquals(
            expected = cidValue,
            actual = DefaultSpaceInviteResolver.parseContentId(link)
        )

        assertEquals(
            expected = fileKeyValue,
            actual = DefaultSpaceInviteResolver.parseFileKey(link)
        )
    }

    @Test
    fun `should parse file key and content id`() {
        val fileKeyValue = "ae62abf2a19e"
        val cidValue = "75538c6c50351eca"

        val link = "chrono://invite/?cid=$cidValue&key=$fileKeyValue"

        assertEquals(
            expected = cidValue,
            actual = DefaultSpaceInviteResolver.parseContentId(link)
        )

        assertEquals(
            expected = fileKeyValue,
            actual = DefaultSpaceInviteResolver.parseFileKey(link)
        )
    }

}