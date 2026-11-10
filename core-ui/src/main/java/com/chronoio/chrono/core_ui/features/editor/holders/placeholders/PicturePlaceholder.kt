package com.chronoio.chrono.core_ui.features.editor.holders.placeholders

import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.core_ui.databinding.ItemBlockMediaPlaceholderBinding
import com.chronoio.chrono.core_ui.extensions.drawable
import com.chronoio.chrono.presentation.editor.editor.listener.ListenerType

class PicturePlaceholder(binding: ItemBlockMediaPlaceholderBinding) : MediaPlaceholder(binding) {

    override fun placeholderClick(target: String, clicked: (ListenerType) -> Unit) {
        clicked(ListenerType.Picture.Placeholder(target))
    }

    override fun setup() {
        title.text = itemView.resources.getString(R.string.hint_upload_image)
        binding.fileIcon.setImageDrawable(itemView.context.drawable(R.drawable.ic_picture))
    }
}