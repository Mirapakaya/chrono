package com.chronoio.chrono.core_ui.features.editor.slash.holders

import androidx.recyclerview.widget.RecyclerView
import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.core_ui.databinding.ItemSlashWidgetStyleBinding
import com.chronoio.chrono.presentation.editor.editor.slash.SlashItem

class MediaMenuHolder(
    val binding: ItemSlashWidgetStyleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SlashItem.Media) = with(binding) {
        when (item) {
            SlashItem.Media.Bookmark -> {
                tvTitle.setText(R.string.slash_widget_media_bookmark)
                tvSubtitle.setText(R.string.slash_widget_media_bookmark_subtitle)
                ivIcon.setImageResource(R.drawable.ic_slash_media_bookmark)
            }
            SlashItem.Media.Code -> {
                tvTitle.setText(R.string.slash_widget_media_code)
                tvSubtitle.setText(R.string.slash_widget_media_code_subtitle)
                ivIcon.setImageResource(R.drawable.ic_slash_media_code)
            }
            SlashItem.Media.File -> {
                tvTitle.setText(R.string.slash_widget_media_file)
                tvSubtitle.setText(R.string.slash_widget_media_file_subtitle)
                ivIcon.setImageResource(R.drawable.ic_slash_media_file)
            }
            SlashItem.Media.Picture -> {
                tvTitle.setText(R.string.slash_widget_media_image)
                tvSubtitle.setText(R.string.slash_widget_media_picture_subtitle)
                ivIcon.setImageResource(R.drawable.ic_slash_media_picture)
            }
            SlashItem.Media.Video -> {
                tvTitle.setText(R.string.slash_widget_media_video)
                tvSubtitle.setText(R.string.slash_widget_media_video_subtitle)
                ivIcon.setImageResource(R.drawable.ic_slash_media_video)
            }
        }
    }
}