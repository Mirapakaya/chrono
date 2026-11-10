package com.chronoio.chrono.core_ui.features.editor.holders.other

import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.core_ui.databinding.ItemBlockUnsupportedBinding
import com.chronoio.chrono.core_ui.features.editor.BlockViewHolder
import com.chronoio.chrono.core_utils.ext.dimen
import com.chronoio.chrono.presentation.editor.editor.model.BlockView

class Unsupported(
    val binding: ItemBlockUnsupportedBinding
) : BlockViewHolder(binding.root), BlockViewHolder.IndentableHolder {
    fun bind(item: BlockView.Unsupported) {
        indentize(item)
        itemView.isSelected = item.isSelected
    }

    override fun indentize(item: BlockView.Indentable) {
        binding.indentator.updateLayoutParams<LinearLayout.LayoutParams> {
            width = item.indent * dimen(R.dimen.indent)
        }
    }
}