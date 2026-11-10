package com.chronoio.chrono.ui.sets.modals.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_ui.extensions.icon
import com.chronoio.chrono.core_ui.extensions.setInputTypeBaseOnFormat
import com.chronoio.chrono.core_ui.reactive.clicks
import com.chronoio.chrono.core_utils.ext.arg
import com.chronoio.chrono.core_utils.ext.gone
import com.chronoio.chrono.core_utils.ext.subscribe
import com.chronoio.chrono.core_utils.ext.visible
import com.chronoio.chrono.core_utils.ui.BaseBottomSheetFragment
import com.chronoio.chrono.databinding.FragmentCreateOrUpdateFilterInputFieldValueBinding
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.presentation.extension.getTextValue
import com.chronoio.chrono.presentation.sets.filter.FilterViewModel
import com.chronoio.chrono.presentation.sets.model.Viewer
import com.chronoio.chrono.ui.sets.modals.PickFilterConditionFragment
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

open class ModifyFilterFromInputFieldValueFragment :
    BaseBottomSheetFragment<FragmentCreateOrUpdateFilterInputFieldValueBinding>(),
    UpdateConditionActionReceiver {

    private val ctx: String get() = arg(CTX_KEY)
    private val space: String get() = arg(SPACE_ID_KEY)
    private val relation: String get() = arg(RELATION_KEY)
    private val index: Int get() = arg(IDX_KEY)
    private val viewer: String get() = arg(VIEWER_KEY)

    @Inject
    lateinit var factory: FilterViewModel.Factory

    private val vm: FilterViewModel by viewModels { factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBottomAction.setText(R.string.apply)
        with(lifecycleScope) {
            subscribe(binding.btnBottomAction.clicks()) {
                vm.onModifyApplyClicked(
                    ctx = ctx,
                    viewerId = viewer,
                    input = binding.enterTextValueInputField.text.toString()
                )
            }
            subscribe(binding.tvFilterCondition.clicks()) {
                vm.onConditionClicked()
            }
        }
    }

    private fun setupJobs() {
        jobs += lifecycleScope.subscribe(vm.relationState.filterNotNull()) {
            binding.tvRelationName.text = it.title
            binding.ivRelationIcon.setImageResource(it.icon())
            binding.enterTextValueInputField.setInputTypeBaseOnFormat(it.format)
        }
        jobs += lifecycleScope.subscribe(vm.filterValueState) { value ->
            binding.enterTextValueInputField.setText(value.getTextValue())
        }
        jobs += lifecycleScope.subscribe(vm.isCompleted) { isCompleted ->
            if (isCompleted) dismiss()
        }
        jobs += lifecycleScope.subscribe(vm.conditionState) {
            binding.tvFilterCondition.text = it?.condition?.title
        }
        jobs += lifecycleScope.subscribe(vm.commands) { observeCommands(it) }
    }

    private fun observeCommands(commands: FilterViewModel.Commands) {
        when (commands) {
            is FilterViewModel.Commands.OpenConditionPicker -> {
                PickFilterConditionFragment.new(
                    ctx = ctx,
                    space = space,
                    mode = PickFilterConditionFragment.MODE_MODIFY,
                    type = commands.type,
                    index = commands.index
                ).showChildFragment()
            }
            FilterViewModel.Commands.HideInput -> {
                binding.enterTextValueInputField.gone()
            }
            FilterViewModel.Commands.ShowInput -> {
                binding.enterTextValueInputField.visible()
            }
            else -> {}
        }
    }

    override fun onStart() {
        setupJobs()
        super.onStart()
        vm.onStart(
            viewerId = viewer,
            relationKey = relation,
            filterIndex = index
        )
    }

    override fun onStop() {
        super.onStop()
        vm.onStop()
    }

    override fun update(condition: Viewer.Filter.Condition) {
        vm.onConditionUpdate(condition)
    }

    override fun injectDependencies() {
        componentManager()
            .modifyFilterComponent
            .get(
                key = ctx,
                param = DefaultComponentParam(
                    ctx = ctx,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().modifyFilterComponent.release(ctx)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateOrUpdateFilterInputFieldValueBinding = FragmentCreateOrUpdateFilterInputFieldValueBinding.inflate(
        inflater, container, false
    )

    companion object {
        fun new(ctx: Id, space: Id, relation: Id, index: Int, viewer: Id) =
            ModifyFilterFromInputFieldValueFragment().apply {
                arguments = bundleOf(
                    CTX_KEY to ctx,
                    SPACE_ID_KEY to space,
                    RELATION_KEY to relation,
                    IDX_KEY to index,
                    VIEWER_KEY to viewer
                )
            }

        const val CTX_KEY = "arg.modify-filter-relation.ctx"
        const val SPACE_ID_KEY = "arg.modify-filter-relation.space-id"
        const val RELATION_KEY = "arg.modify-filter-relation.relation"
        const val IDX_KEY = "arg.modify-filter-relation.index"
        private const val VIEWER_KEY = "arg.modify-filter-relation.viewer"
    }
}