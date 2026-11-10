package com.chronoio.chrono.core_ui.features.editor.slash.holders

import androidx.recyclerview.widget.RecyclerView
import com.chronoio.chrono.core_ui.databinding.ItemSlashWidgetObjectTypeBinding
import com.chronoio.chrono.core_utils.ext.gone
import com.chronoio.chrono.core_utils.ext.visible
import com.chronoio.chrono.presentation.editor.editor.slash.SlashItem

class ObjectTypeMenuHolder(
    val binding: ItemSlashWidgetObjectTypeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SlashItem.ObjectType) = with(binding) {
        val objectType = item.objectTypeView
        ivIcon.setIcon(objectType.icon)
        tvTitle.text = objectType.name
        if (objectType.description.isNullOrBlank()) {
            tvSubtitle.gone()
        } else {
            tvSubtitle.visible()
            tvSubtitle.text = objectType.description
        }
    }
}