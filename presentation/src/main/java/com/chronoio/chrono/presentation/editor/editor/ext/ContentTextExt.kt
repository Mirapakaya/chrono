package com.chronoio.chrono.presentation.editor.editor.ext

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.Block.Content
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.ext.content
import com.chronoio.chrono.core_models.ext.replaceRangeWithWord
import com.chronoio.chrono.core_models.ext.title
import com.chronoio.chrono.core_models.restrictions.ObjectRestriction
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.presentation.BuildConfig
import com.chronoio.chrono.core_models.ObjectViewDetails
import com.chronoio.chrono.presentation.editor.editor.Markup
import com.chronoio.chrono.presentation.extension.getDateObject
import com.chronoio.chrono.presentation.extension.getObject
import com.chronoio.chrono.presentation.extension.shift
import com.chronoio.chrono.presentation.widgets.collection.ResourceProvider
import timber.log.Timber

fun Block.Content.Text.getTextAndMarks(
    details: ObjectViewDetails,
    marks: List<Markup.Mark>,
    fieldParser: FieldParser,
    resourceProvider: ResourceProvider
): Pair<String, List<Markup.Mark>> {

    if (marks.none { it is Markup.Mark.Mention }) {
        return text to marks
    }
    var updatedText = text
    val updatedMarks = marks.toMutableList()
    updatedMarks.sortBy { it.from }
    try {
        updatedMarks.forEach { mark ->
            if (mark !is Markup.Mark.Mention || mark.param.isBlank()) return@forEach
            val newName = when (mark) {
                is Markup.Mark.Mention.Date -> getFormattedDateMention(
                    mark = mark,
                    details = details,
                    fieldParser = fieldParser,
                    resourceProvider = resourceProvider
                )
                is Markup.Mark.Mention.Deleted -> resourceProvider.getNonExistentObjectTitle()
                else -> {
                    val obj = details.getObject(mark.param)
                    if (obj != null) {
                        fieldParser.getObjectNameOrPluralsForTypes(obj)
                    } else {
                        return@forEach
                    }
                }
            }
            val oldName = updatedText.substring(mark.from, mark.to)
            val finalName =
                if (newName.isNullOrBlank()) resourceProvider.getUntitledTitle() else newName

            if (finalName != oldName) {
                val lengthDifference = finalName.length - oldName.length
                updatedText = updatedText.replaceRangeWithWord(
                    replace = finalName,
                    from = mark.from,
                    to = mark.to
                )
                updatedMarks.shift(
                    start = mark.from,
                    length = lengthDifference
                )
            }
        }
    } catch (e: Exception) {
        Timber.e(e, "Error while update mention markups")
        return text to marks
    }
    return updatedText to updatedMarks
}

private fun Block.Content.Text.getFormattedDateMention(
    mark: Markup.Mark.Mention.Date,
    details: ObjectViewDetails,
    fieldParser: FieldParser,
    resourceProvider: ResourceProvider
): String? {
    val dateObject = details.getDateObject(id = mark.param)
    return if (BuildConfig.ENABLE_RELATIVE_DATES_IN_MENTIONS) {
        val timestamp = dateObject?.timestamp
        val relativeDate = fieldParser.toDate(timestamp)?.relativeDate
        resourceProvider.toFormattedString(relativeDate = relativeDate).takeIf { it.isNotEmpty() }
    } else {
        dateObject?.name
    }
}

fun List<Block>.isAllowedToShowTypesWidget(
    objectRestrictions: List<ObjectRestriction>,
    isOwnerOrEditor: Boolean,
    objectLayout: ObjectType.Layout?
): Boolean {
    if (objectRestrictions.any { it == ObjectRestriction.TYPE_CHANGE }) return false
    if (!isOwnerOrEditor) return false
    return if (objectLayout == ObjectType.Layout.NOTE) {
        return true
    } else {
        return title()?.content<Content.Text>()?.text?.isEmpty() == true
    }
}

