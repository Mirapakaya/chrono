package com.chronoio.chrono.domain.primitives

import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.objects.GetDateObjectByTimestamp
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.test_utils.MockDataFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Regression coverage for DROID-4517 / IOS-6143:
 * widget and list elements must show an object's own title, not its type's plural name.
 * The plural name is only valid when the object itself is a type (layout == OBJECT_TYPE).
 */
class FieldParserGetObjectNameOrPluralsForTypesTest {

    private val dateProvider: DateProvider = mock()
    private val logger: Logger = mock()
    private val getDateObjectByTimestamp: GetDateObjectByTimestamp = mock()
    private val stringResourceProvider: StringResourceProvider = mock()

    private val fieldParser = FieldParserImpl(
        dateProvider = dateProvider,
        logger = logger,
        getDateObjectByTimestamp = getDateObjectByTimestamp,
        stringResourceProvider = stringResourceProvider
    )

    companion object {
        private const val UNTITLED = "Untitled"
    }

    @Test
    fun `should use object own name for regular object even when plural name is present`() {
        // Given a regular object whose details carry a (type-derived) plural name
        val objectName = "Lenovo Legion Cooling Update"
        val obj = ObjectWrapper.Basic(
            map = mapOf(
                Relations.ID to MockDataFactory.randomUuid(),
                Relations.LAYOUT to ObjectType.Layout.BASIC.code.toDouble(),
                Relations.NAME to objectName,
                Relations.PLURAL_NAME to "Regular Entries"
            )
        )

        // When
        val result = fieldParser.getObjectNameOrPluralsForTypes(obj)

        // Then the object's own title wins, not the plural name
        assertEquals(objectName, result)
    }

    @Test
    fun `should use plural name when object itself is a type`() {
        // Given an object-type object with a plural name
        val pluralName = "Journal Entries"
        val obj = ObjectWrapper.Basic(
            map = mapOf(
                Relations.ID to MockDataFactory.randomUuid(),
                Relations.LAYOUT to ObjectType.Layout.OBJECT_TYPE.code.toDouble(),
                Relations.NAME to "Journal Entry",
                Relations.PLURAL_NAME to pluralName
            )
        )

        // When
        val result = fieldParser.getObjectNameOrPluralsForTypes(obj)

        // Then the plural name is used for the type
        assertEquals(pluralName, result)
    }

    @Test
    fun `should fall back to singular name for type when plural name is empty`() {
        // Given an object-type object without a plural name
        val typeName = "Journal Entry"
        val obj = ObjectWrapper.Basic(
            map = mapOf(
                Relations.ID to MockDataFactory.randomUuid(),
                Relations.LAYOUT to ObjectType.Layout.OBJECT_TYPE.code.toDouble(),
                Relations.NAME to typeName,
                Relations.PLURAL_NAME to ""
            )
        )

        // When
        val result = fieldParser.getObjectNameOrPluralsForTypes(obj)

        // Then
        assertEquals(typeName, result)
    }

    @Test
    fun `should return untitled for blank regular object when useUntitled is true`() {
        // Given a blank-named regular object
        whenever(stringResourceProvider.getUntitledObjectTitle()).thenReturn(UNTITLED)
        val obj = ObjectWrapper.Basic(
            map = mapOf(
                Relations.ID to MockDataFactory.randomUuid(),
                Relations.LAYOUT to ObjectType.Layout.BASIC.code.toDouble(),
                Relations.NAME to ""
            )
        )

        // When
        val result = fieldParser.getObjectNameOrPluralsForTypes(obj, useUntitled = true)

        // Then
        assertEquals(UNTITLED, result)
    }

    @Test
    fun `should return empty string for blank regular object when useUntitled is false`() {
        // Given a blank-named regular object
        val obj = ObjectWrapper.Basic(
            map = mapOf(
                Relations.ID to MockDataFactory.randomUuid(),
                Relations.LAYOUT to ObjectType.Layout.BASIC.code.toDouble(),
                Relations.NAME to ""
            )
        )

        // When
        val result = fieldParser.getObjectNameOrPluralsForTypes(obj, useUntitled = false)

        // Then no "Untitled" fallback is applied (preserves widget behavior)
        assertEquals("", result)
    }
}
