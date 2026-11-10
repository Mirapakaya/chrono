package com.chronoio.chrono.core_ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.core_ui.databinding.WidgetDvGridObjectBinding
import com.chronoio.chrono.core_ui.extensions.color
import com.chronoio.chrono.core_utils.ext.gone
import com.chronoio.chrono.core_utils.ext.visible
import com.chronoio.chrono.core_models.ui.ObjectIcon

class GridCellObjectItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    val binding = WidgetDvGridObjectBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun setup(name: String, icon: ObjectIcon) = with(binding) {
        tvName.visible()
        tvName.text = name
        tvName.setTextColor(context.color(R.color.text_primary))
        when (icon) {
            ObjectIcon.None -> {
                objectIcon.gone()
                tvName.updateLayoutParams<LayoutParams> {
                    marginStart = 0
                }
            }
            else -> {
                objectIcon.visible()
                objectIcon.setIcon(icon)
                tvName.updateLayoutParams<LayoutParams> {
                    marginStart = resources.getDimension(R.dimen.dp_20).toInt()
                }
            }
        }
    }

    fun setupAsNonExistent() = with(binding) {
        tvName.visible()
        tvName.setText(R.string.non_existent_object)
        tvName.setTextColor(context.color(R.color.text_tertiary))
        objectIcon.setNonExistentIcon()
    }
}