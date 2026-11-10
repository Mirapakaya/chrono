package com.chronoio.chrono.core_ui.features.editor.holders.error

import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.core_ui.databinding.ItemBlockMediaErrorBinding
import com.chronoio.chrono.core_ui.extensions.drawable
import com.chronoio.chrono.core_ui.features.editor.decoration.DecoratableCardViewHolder
import com.chronoio.chrono.presentation.editor.editor.listener.ListenerType
import com.chronoio.chrono.presentation.editor.editor.model.BlockView

class VideoError(
    binding: ItemBlockMediaErrorBinding
) : MediaError(binding) {

    override fun errorClick(item: BlockView.Error, clicked: (ListenerType) -> Unit) {
        if (item is BlockView.Error.Video) {
            clicked(ListenerType.Video.Error(item.id))
        }
    }

    override fun bind(item: BlockView.Error, clicked: (ListenerType) -> Unit) {
        super.bind(item, clicked)
        if (item is BlockView.Error.Video) {
            binding.fileName.text = if (item.name.isNullOrBlank()) {
                itemView.resources.getString(R.string.hint_upload_video)
            } else
                item.name
        }
        binding.fileIcon.setImageDrawable(itemView.context.drawable(R.drawable.ic_video_inactive))
    }
}