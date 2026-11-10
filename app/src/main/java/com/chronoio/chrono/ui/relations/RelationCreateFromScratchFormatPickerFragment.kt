package com.chronoio.chrono.ui.relations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Relation
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_ui.features.relations.RelationFormatAdapter
import com.chronoio.chrono.core_utils.ext.arg
import com.chronoio.chrono.core_utils.ext.drawable
import com.chronoio.chrono.core_utils.ui.BaseBottomSheetFragment
import com.chronoio.chrono.databinding.FragmentRelationCreateFromScratchFormatPickerBinding
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.presentation.relations.model.CreateFromScratchState
import com.chronoio.chrono.presentation.relations.model.RelationView
import com.chronoio.chrono.presentation.relations.model.StateHolder
import javax.inject.Inject

class RelationCreateFromScratchFormatPickerFragment :
    BaseBottomSheetFragment<FragmentRelationCreateFromScratchFormatPickerBinding>() {

    private val ctx get() = arg<Id>(CTX_KEY)
    private val space get() = arg<Id>(SPACE_KEY)
    private val flow get() = arg<Id>(FLOW_TYPE)

    @Inject
    lateinit var stateHolder: StateHolder<CreateFromScratchState>

    private val relationAdapter = RelationFormatAdapter(
        onItemClick = { f ->
            stateHolder.state.value = stateHolder.state.value.copy(
                format = f.format,
                limitObjectTypes = emptyList()
            )
            dismiss()
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.connectWithRecycler.apply {
            adapter = relationAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(drawable(R.drawable.divider_relations))
                }
            )
        }
        relationAdapter.submitList(
            Relation.orderedFormatList()
                .filter { !excludedFormats.contains(it) }
                .map { format ->
                    RelationView.CreateFromScratch(
                        format = format,
                        isSelected = format == stateHolder.state.value.format
                    )
                }
        )
    }

    override fun injectDependencies() {
        val param = DefaultComponentParam(
            ctx = ctx,
            space = SpaceId(space)
        )
        when (flow) {
            FLOW_OBJECT -> {
                componentManager().relationFormatPickerObjectComponent.get(param).inject(this)
            }
            FLOW_BLOCK -> {
                componentManager().relationFormatPickerBlockComponent.get(param).inject(this)
            }
            FLOW_DV -> {
                componentManager().relationFormatPickerDataViewComponent.get(param).inject(this)
            }
            FLOW_LIBRARY -> {
                componentManager().relationFormatPickerLibraryComponent.get(ctx).inject(this)
            }
            FLOW_SET_OR_COLLECTION -> {
                componentManager().relationFormatPickerSetOrCollectionComponent.get(param).inject(this)
            }
        }
    }

    override fun releaseDependencies() {
        when (flow) {
            FLOW_OBJECT -> {
                componentManager().relationFormatPickerObjectComponent.release()
            }
            FLOW_BLOCK -> {
                componentManager().relationFormatPickerBlockComponent.release()
            }
            FLOW_DV -> {
                componentManager().relationFormatPickerDataViewComponent.release()
            }
            FLOW_LIBRARY -> {
                componentManager().relationFormatPickerLibraryComponent.release()
            }
            FLOW_SET_OR_COLLECTION -> {
                componentManager().relationFormatPickerSetOrCollectionComponent.release()
            }
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRelationCreateFromScratchFormatPickerBinding =
        FragmentRelationCreateFromScratchFormatPickerBinding.inflate(
            inflater, container, false
        )

    companion object {
        const val CTX_KEY = "arg.relation-format-picker.ctx"
        const val SPACE_KEY = "arg.relation-format-picker.space"
        const val FLOW_TYPE = "arg.relation-format-picker.flow"
        val excludedFormats = listOf(
            Relation.Format.SHORT_TEXT,
            Relation.Format.EMOJI,
            Relation.Format.RELATIONS
        )
        const val FLOW_OBJECT = "arg.relation-format-picker.flow-object"
        const val FLOW_DV = "arg.relation-format-picker.flow-dv"
        const val FLOW_BLOCK = "arg.relation-format-picker.flow-block"
        const val FLOW_LIBRARY = "arg.relation-format-picker.flow-library"
        const val FLOW_SET_OR_COLLECTION = "arg.relation-format-picker.flow-set-or-collection"
    }
}