package com.chronoio.chrono.core_ui.features.editor.holders.upload

import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import com.chronoio.chrono.core_ui.databinding.ItemBlockMediaPlaceholderBinding
import com.chronoio.chrono.core_ui.features.editor.BlockViewDiffUtil
import com.chronoio.chrono.core_ui.features.editor.BlockViewHolder
import com.chronoio.chrono.core_ui.features.editor.EditorTouchProcessor
import com.chronoio.chrono.core_ui.features.editor.SupportCustomTouchProcessor
import com.chronoio.chrono.core_ui.features.editor.decoration.DecoratableCardViewHolder
import com.chronoio.chrono.core_ui.features.editor.decoration.EditorDecorationContainer
import com.chronoio.chrono.core_ui.features.editor.decoration.applySelectorOffset
import com.chronoio.chrono.presentation.editor.editor.listener.ListenerType
import com.chronoio.chrono.presentation.editor.editor.model.BlockView

abstract class MediaUpload(
    val binding: ItemBlockMediaPlaceholderBinding
) : BlockViewHolder(binding.root),
    BlockViewHolder.IndentableHolder,
    SupportCustomTouchProcessor, DecoratableCardViewHolder {

    override val decoratableContainer: EditorDecorationContainer get() = binding.decorationContainer
    override val decoratableCard: View get() = binding.card
    abstract fun uploadClick(target: String, clicked: (ListenerType) -> Unit)

    override val editorTouchProcessor = EditorTouchProcessor(
        touchSlop = ViewConfiguration.get(itemView.context).scaledTouchSlop,
        fallback = { e -> itemView.onTouchEvent(e) }
    )

    init {
        itemView.setOnTouchListener { v, e -> editorTouchProcessor.process(v, e) }
    }

    open fun bind(
        item: BlockView.Upload,
        clicked: (ListenerType) -> Unit
    ) {
        select(item.isSelected)
        with(itemView) {
            setOnClickListener { uploadClick(item.id, clicked) }
        }
    }

    open fun processChangePayload(payloads: List<BlockViewDiffUtil.Payload>, item: BlockView) {
        check(item is BlockView.Upload) { "Expected upload block, but was: $item" }
        payloads.forEach { payload ->
            if (payload.isSelectionChanged) {
                select(item.isSelected)
            }
        }
    }

    fun select(isSelected: Boolean) {
        binding.selected.isSelected = isSelected
    }

    @Deprecated("Pre-nested-styling legacy.")
    override fun indentize(item: BlockView.Indentable) {
        // Do nothing.
    }

    override fun applyDecorations(decorations: List<BlockView.Decoration>) {
        super.applyDecorations(decorations)
        binding.selected.applySelectorOffset<FrameLayout.LayoutParams>(
            content = binding.card,
            res = itemView.resources
        )
    }
}