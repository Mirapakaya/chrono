package com.chronoio.chrono.core_ui.features.editor.holders.other

import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.chronoio.chrono.core_ui.BuildConfig
import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.core_ui.databinding.ItemBlockObjectLinkLoadingBinding
import com.chronoio.chrono.core_ui.features.editor.BlockViewDiffUtil
import com.chronoio.chrono.core_ui.features.editor.BlockViewHolder
import com.chronoio.chrono.core_utils.ext.dimen
import com.chronoio.chrono.presentation.editor.editor.listener.ListenerType
import com.chronoio.chrono.presentation.editor.editor.model.BlockView

class LinkToObjectLoading(binding: ItemBlockObjectLinkLoadingBinding) :
    BlockViewHolder(binding.root),
    BlockViewHolder.IndentableHolder {

    private val container = binding.container

    fun bind(
        item: BlockView.LinkToObject.Loading,
        clicked: (ListenerType) -> Unit
    ) {

        applySelectedState(item)

        itemView.setOnClickListener { clicked(ListenerType.LinkToObjectLoading(item.id)) }
    }

    private fun applySelectedState(item: BlockView.LinkToObject.Loading) {
        container.isSelected = item.isSelected
    }

    fun processChangePayload(payloads: List<BlockViewDiffUtil.Payload>, item: BlockView) {
        check(item is BlockView.LinkToObject.Loading) { "Expected a link to object block, but was: $item" }
        payloads.forEach { payload ->
            if (payload.isSelectionChanged) {
                applySelectedState(item)
            }
        }
    }

    @Deprecated("Pre-nested-styling legacy.")
    override fun indentize(item: BlockView.Indentable) {
        // Do nothing.
    }
}