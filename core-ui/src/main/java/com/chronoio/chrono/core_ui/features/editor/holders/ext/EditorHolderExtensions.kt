package com.chronoio.chrono.core_ui.features.editor.holders.ext

import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.updatePadding
import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.core_ui.features.editor.BlockAdapter
import com.chronoio.chrono.core_ui.features.editor.holders.relations.RelationBlockViewHolder
import com.chronoio.chrono.core_utils.ext.dimen
import com.chronoio.chrono.presentation.editor.editor.listener.ListenerType
import com.chronoio.chrono.presentation.editor.editor.model.BlockView

fun RelationBlockViewHolder.setup(adapter: BlockAdapter): RelationBlockViewHolder {
    with(itemView) {
        findViewById<ViewGroup?>(R.id.itemContainer)?.updatePadding(
            top = dimen(R.dimen.relation_view_padding_top),
            bottom = dimen(R.dimen.relation_view_padding_bottom)
        )
        val paddingStart = context.dimen(R.dimen.default_document_content_padding_start)
        val paddingEnd = context.dimen(R.dimen.default_document_content_padding_end)
        findViewById<ViewGroup>(R.id.content).apply {
            setBackgroundResource(R.drawable.item_block_code_multi_select_mode_selector)
            updatePadding(left = paddingStart.toInt(), right = paddingEnd.toInt())
        }
        setOnClickListener {
            val view = adapter.views[bindingAdapterPosition]
            check(view is BlockView.Relation)
            adapter.onClickListener(ListenerType.Relation.Related(view))
        }
    }
    return this
}

fun RelationBlockViewHolder.setupPlaceholder(adapter: BlockAdapter): RelationBlockViewHolder {
    with(itemView) {
        val paddingStart = context.dimen(R.dimen.default_document_content_padding_start)
        val paddingEnd = context.dimen(R.dimen.default_document_content_padding_end)
        findViewById<ViewGroup>(R.id.content).apply {
            setBackgroundResource(R.drawable.item_block_code_multi_select_mode_selector)
            updatePadding(left = paddingStart.toInt(), right = paddingEnd.toInt())
        }
        setOnClickListener {
            val view = adapter.views[bindingAdapterPosition]
            check(view is BlockView.Relation)
            adapter.onClickListener(ListenerType.Relation.Placeholder(view.id))
        }
        setOnLongClickListener {
            val view = adapter.views[bindingAdapterPosition]
            check(view is BlockView.Relation)
            this@setupPlaceholder.onBlockLongClick(
                root = this,
                target = view.id,
                clicked = adapter.onClickListener
            )
            true
        }
    }
    return this
}

fun BlockView.InputAction.toIMECode(): Int = when (this) {
    BlockView.InputAction.Done -> EditorInfo.IME_ACTION_DONE
    BlockView.InputAction.NewLine -> EditorInfo.IME_ACTION_GO
}