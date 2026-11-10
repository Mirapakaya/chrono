package com.chronoio.chrono.core_ui.features.dataview.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.core_ui.extensions.getPrettyName
import com.chronoio.chrono.presentation.sets.model.CellView

class DVGridCellDateHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(cell: CellView.Date) {
        val prettyDate = cell.relativeDate?.getPrettyName(
            resources = itemView.resources,
            isTimeIncluded = cell.isTimeIncluded
        )
        itemView.findViewById<TextView>(R.id.tvText).text = prettyDate
    }
}