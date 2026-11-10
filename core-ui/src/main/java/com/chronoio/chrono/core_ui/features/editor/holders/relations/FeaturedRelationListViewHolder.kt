package com.chronoio.chrono.core_ui.features.editor.holders.relations

import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.core_ui.databinding.ItemBlockFeaturedRelationsBinding
import com.chronoio.chrono.core_ui.features.editor.BlockViewHolder
import com.chronoio.chrono.core_utils.ext.dimen
import com.chronoio.chrono.presentation.editor.editor.listener.ListenerType
import com.chronoio.chrono.presentation.editor.editor.model.BlockView

class FeaturedRelationListViewHolder(
    val binding: ItemBlockFeaturedRelationsBinding
) : BlockViewHolder(binding.root) {

    private val root = binding.featuredRelationRoot

    fun bind(item: BlockView.FeaturedRelation, click: (ListenerType) -> Unit) {
        if (item.isTodoLayout) {
            val lr = itemView.context.dimen(R.dimen.dp_60).toInt()
            binding.featuredRelationRoot.setPadding(lr, 0, 0, 0)
        } else {
            val lr = itemView.context.dimen(R.dimen.dp_20).toInt()
            binding.featuredRelationRoot.setPadding(lr, 0, 0, 0)
        }
        root.set(item, click)
    }
}