package com.chronoio.chrono.core_ui.widgets.toolbar

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.core_ui.features.editor.MarkupColorAdapter
import com.chronoio.chrono.core_ui.layout.SpacingItemDecoration
import com.chronoio.chrono.core_utils.ext.dimen
import com.chronoio.chrono.core_models.ThemeColor
import com.chronoio.chrono.presentation.editor.markup.MarkupColorView

class MarkupColorToolbarWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    var onColorClickedListener: (MarkupColorView) -> Unit = {}

    private val markupColorAdapter = MarkupColorAdapter(
        items = emptyList(),
        onColorClicked = { color -> onColorClickedListener(color) }
    )

    fun setTextColor(code: String) {
        markupColorAdapter.update(
            ThemeColor.values().map { color ->
                MarkupColorView.Text(
                    code = color.code,
                    isSelected = color.code == code
                )
            }
        )
    }

    fun setBackgroundColor(code: String) {
        markupColorAdapter.update(
            ThemeColor.values().map { color ->
                MarkupColorView.Background(
                    code = color.code,
                    isSelected = color.code == code
                )
            }
        )
    }

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        addItemDecoration(
            SpacingItemDecoration(
                firstItemSpacingStart = context.dimen(R.dimen.dp_6).toInt(),
                lastItemSpacingEnd = context.dimen(R.dimen.dp_6).toInt()
            )
        )
        adapter = markupColorAdapter
    }
}