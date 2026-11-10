package com.chronoio.chrono.core_ui.features.editor.holders.upload

import android.view.View
import com.chronoio.chrono.core_ui.databinding.ItemBlockOpenFileBinding
import com.chronoio.chrono.core_ui.features.editor.BlockViewHolder
import com.chronoio.chrono.presentation.editor.editor.listener.ListenerType
import com.chronoio.chrono.presentation.editor.editor.model.BlockView

class OpenFile(
    binding: ItemBlockOpenFileBinding
) : BlockViewHolder(binding.root) {

    private val root: View = itemView

    fun bind(item: BlockView.ButtonOpenFile.FileButton, click: (ListenerType) -> Unit) {
        root.setOnClickListener {
            click(
                ListenerType.File.View(
                    target = item.id,
                )
            )
        }
    }
}

class OpenImage(
    binding: ItemBlockOpenFileBinding
) : BlockViewHolder(binding.root) {

    private val root: View = itemView

    fun bind(item: BlockView.ButtonOpenFile.ImageButton, click: (ListenerType) -> Unit) {
        root.setOnClickListener {
            click(
                ListenerType.Picture.View(
                    target = item.id,
                    obj = item.targetId
                )
            )
        }
    }
}