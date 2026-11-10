package com.chronoio.chrono.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.chronoio.chrono.R
import com.chronoio.chrono.core_ui.reactive.clicks
import com.chronoio.chrono.core_utils.ext.arg
import com.chronoio.chrono.core_utils.ui.BaseBottomSheetFragment
import com.chronoio.chrono.databinding.FragmentDeleteAlertBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DeleteAlertFragment : BaseBottomSheetFragment<FragmentDeleteAlertBinding>() {

    private val count get() = arg<Int>(COUNT_KEY)

    var onDeletionAccepted: () -> Unit = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (count > 1) {
            binding.tvTitle.text = getString(R.string.are_you_sure_delete_n_objects, count)
            binding.tvSubtitle.setText(R.string.delete_irrevocably)
        } else {
            binding.tvTitle.setText(R.string.are_you_sure_delete_one_object)
            binding.tvSubtitle.setText(R.string.delete_irrevocably_one_object)
        }

        binding.btnCancel
            .clicks()
            .onEach { dismiss() }
            .launchIn(lifecycleScope)

        binding.btnDelete
            .clicks()
            .onEach {
                onDeletionAccepted()
                dismiss()
            }
            .launchIn(lifecycleScope)
    }

    override fun injectDependencies() {}
    override fun releaseDependencies() {}

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDeleteAlertBinding = FragmentDeleteAlertBinding.inflate(
        inflater, container, false
    )

    companion object {
        const val COUNT_KEY = "arg.delete-alert-dialog.count"
        fun new(count: Int) : DeleteAlertFragment = DeleteAlertFragment().apply {
            arguments = bundleOf(COUNT_KEY to count)
        }
    }
}