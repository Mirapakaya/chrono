package com.chronoio.chrono.core_ui.features.dataview.holders

import androidx.recyclerview.widget.RecyclerView
import com.chronoio.chrono.core_ui.databinding.ItemViewerGridCellTagBinding
import com.chronoio.chrono.core_utils.ext.gone
import com.chronoio.chrono.core_utils.ext.visible
import com.chronoio.chrono.presentation.sets.model.CellView

class DVGridCellTagHolder(val binding: ItemViewerGridCellTagBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cell: CellView.Tag) {
        val first = cell.tags.firstOrNull()
        if (first == null) {
            binding.tagValue.gone()
        } else {
            // Match the List view: first tag chip + a "+N" overflow badge (see
            // ListViewRelationTagValueView), instead of laying out every tag in the fixed column.
            binding.tagValue.visible()
            binding.tagValue.setup(
                name = first.tag,
                tagColor = first.color,
                size = cell.tags.size
            )
        }
    }
}
