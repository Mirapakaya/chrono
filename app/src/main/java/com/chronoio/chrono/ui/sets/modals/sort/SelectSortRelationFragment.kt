package com.chronoio.chrono.ui.sets.modals.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_ui.features.sets.SearchRelationAdapter
import com.chronoio.chrono.core_ui.reactive.textChanges
import com.chronoio.chrono.core_utils.ext.arg
import com.chronoio.chrono.core_utils.ext.drawable
import com.chronoio.chrono.core_utils.ext.invisible
import com.chronoio.chrono.core_utils.ext.subscribe
import com.chronoio.chrono.core_utils.ext.visible
import com.chronoio.chrono.core_utils.ui.BaseBottomSheetTextInputFragment
import com.chronoio.chrono.databinding.FragmentSelectSortOrFilterRelationBinding
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.presentation.sets.SelectSortRelationViewModel
import javax.inject.Inject

class SelectSortRelationFragment : BaseBottomSheetTextInputFragment<FragmentSelectSortOrFilterRelationBinding>() {

    private val ctx: String get() = arg(CTX_KEY)
    private val space: String get() = arg(SPACE_ID_KEY)
    private val viewer: String get() = arg(VIEWER_ID_KEY)

    private val searchRelationAdapter by lazy {
        SearchRelationAdapter { relation ->
            vm.onRelationClicked(ctx = ctx, viewerId = viewer, relation = relation)
        }
    }

    lateinit var searchRelationInput: EditText
    lateinit var clearSearchText: View

    @Inject
    lateinit var factory: SelectSortRelationViewModel.Factory

    private val vm: SelectSortRelationViewModel by viewModels { factory }

    override val textInput: EditText get() = binding.searchBar.root.findViewById(R.id.filterInputField)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchRelationInput = binding.searchBar.root.findViewById(R.id.filterInputField)
        searchRelationInput.apply {
            hint = getString(R.string.choose_relation_to_sort)
        }
        clearSearchText = binding.searchBar.root.findViewById(R.id.clearSearchText)
        clearSearchText.setOnClickListener {
            searchRelationInput.setText("")
            clearSearchText.invisible()

        }
        binding.searchRelationRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchRelationAdapter
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    setDrawable(drawable(R.drawable.divider_sort_or_filter_relation))
                }
            )
        }
        with(lifecycleScope) {
            subscribe(searchRelationInput.textChanges()) {
                vm.onSearchQueryChanged(it.toString())
                if (it.isEmpty()) {
                    clearSearchText.invisible()
                } else {
                    clearSearchText.visible()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        vm.onStart(viewer)
    }

    override fun onStop() {
        super.onStop()
        vm.onStop()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(lifecycleScope) {
            subscribe(vm.views) { searchRelationAdapter.update(it) }
            subscribe(vm.isDismissed) { isDismissed -> if (isDismissed) dismiss() }
        }
    }

    override fun injectDependencies() {
        componentManager()
            .selectSortRelationComponent
            .get(
                params = DefaultComponentParam(
                    ctx = ctx,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().selectSortRelationComponent.release()
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelectSortOrFilterRelationBinding =
        FragmentSelectSortOrFilterRelationBinding.inflate(
            inflater, container, false
        )

    companion object {

        fun new(ctx: Id, space: Id, viewerId: Id): SelectSortRelationFragment = SelectSortRelationFragment().apply {
            arguments = bundleOf(CTX_KEY to ctx, SPACE_ID_KEY to space, VIEWER_ID_KEY to viewerId)
        }

        const val CTX_KEY = "arg.select-sort-relation.ctx"
        const val SPACE_ID_KEY = "arg.select-sort-relation.space-id"
        const val VIEWER_ID_KEY = "arg.select-sort-relation.viewer"
    }
}
