package com.chronoio.chrono.core_ui.features.editor.holders.other

import android.view.View
import androidx.core.view.updatePadding
import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.core_ui.databinding.ItemBlockLatexBinding
import com.chronoio.chrono.core_ui.extensions.setBlockBackgroundColor
import com.chronoio.chrono.core_ui.features.editor.BlockViewDiffUtil
import com.chronoio.chrono.core_ui.features.editor.BlockViewHolder
import com.chronoio.chrono.core_utils.ext.dimen
import com.chronoio.chrono.core_models.ThemeColor
import com.chronoio.chrono.presentation.editor.editor.model.BlockView
import com.judemanutd.katexview.KatexView
import timber.log.Timber

class Latex(
    val binding: ItemBlockLatexBinding
) : BlockViewHolder(binding.root), BlockViewHolder.IndentableHolder {

    val content: View = binding.latexContainer
    val latexView: KatexView = binding.latexView

    init {
        latexView.setTextColor(
            itemView.resources.getColor(R.color.text_primary, null)
        )
    }

    fun bind(item: BlockView.Latex) {
        indentize(item)
        setIsSelected(item)
        setLatex(item.latex)
        setBackground(item.background)
    }

    private fun setIsSelected(item: BlockView.Latex) {
        binding.latexContainer.isSelected = item.isSelected
    }

    private fun setLatex(latex: String) {
        val encode = "$$ $latex $$"
        try {
            binding.latexView.setText(encode)
        } catch (e: Exception) {
            Timber.e(e, "Error while setting latex text")
        }
    }

    private fun setBackground(backgroundColor: ThemeColor) {
        itemView.setBlockBackgroundColor(backgroundColor)
    }

    fun processChangePayload(
        payloads: List<BlockViewDiffUtil.Payload>,
        item: BlockView.Latex
    ) {
        payloads.forEach { payload ->
            if (payload.isLatexChanged) setLatex(item.latex)
            if (payload.isSelectionChanged) setIsSelected(item)
            if (payload.isBackgroundColorChanged) setBackground(item.background)
        }
    }

    override fun indentize(item: BlockView.Indentable) {
        binding.latexView.updatePadding(
            left = dimen(R.dimen.default_document_content_padding_start) + item.indent * dimen(R.dimen.indent)
        )
    }
}