package com.chronoio.chrono.core_ui.features.dataview.holders

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.presentation.sets.model.CellView

class DVGridCellCheckboxHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(cell: CellView.Checkbox) {
        itemView.findViewById<ImageView>(R.id.ivCheckbox).isSelected = cell.isChecked
    }
}