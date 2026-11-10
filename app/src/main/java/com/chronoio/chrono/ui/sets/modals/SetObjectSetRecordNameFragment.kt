package com.chronoio.chrono.ui.sets.modals

import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
import android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_ui.reactive.textChanges
import com.chronoio.chrono.core_utils.ext.argString
import com.chronoio.chrono.core_utils.ext.subscribe
import com.chronoio.chrono.databinding.FragmentSetObjectSetRecordNameBinding
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.presentation.sets.ObjectSetRecordViewModel
import com.chronoio.chrono.presentation.sets.ObjectSetRecordViewModel.Command
import com.chronoio.chrono.ui.editor.EditorFragment
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SetObjectSetRecordNameFragment : SetObjectCreateRecordFragmentBase<FragmentSetObjectSetRecordNameBinding>() {

    override val textInputType: Int = TYPE_CLASS_TEXT or TYPE_TEXT_FLAG_CAP_SENTENCES or TYPE_TEXT_FLAG_AUTO_CORRECT
    override val textInputField: EditText
        get() = binding.textInputField
    override val button: AppCompatImageButton
        get() = binding.icExpand

    @Inject
    lateinit var factory: ObjectSetRecordViewModel.Factory
    override val vm: ObjectSetRecordViewModel by viewModels { factory }

    private val target: String get() = argString(TARGET_KEY)

    override fun onStart(scope: CoroutineScope) {
        super.onStart(scope)
        lifecycleScope.launch {
            jobs += subscribe(vm.commands) { subscribeCommands(it) }
            jobs += subscribe(textInputField.textChanges()) { newText ->
                vm.onTextChanged(
                    input = newText.toString(),
                    target = target
                )
            }
        }
    }

    private fun subscribeCommands(command: Command) {
        when (command) {
            is Command.OpenObject -> {
                findNavController().navigate(
                    R.id.objectNavigation,
                    EditorFragment.args(
                        ctx = command.ctx,
                        space = command.space
                    )
                )
            }
        }
    }

    override fun onButtonClicked() {
        vm.onButtonClicked(
            target = target,
            input = textInputField.text.toString(),
            space = space
        )
    }

    override fun onKeyboardActionDone() {
        vm.onActionDone(
            target = target,
            input = textInputField.text.toString(),
            space = space
        )
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSetObjectSetRecordNameBinding = FragmentSetObjectSetRecordNameBinding.inflate(
        inflater, container, false
    )

    override fun injectDependencies() {
        componentManager()
            .objectSetRecordComponent.get(
                DefaultComponentParam(
                    ctx = ctx,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().objectSetRecordComponent.release()
    }

    companion object {
        private const val ARG_CTX_KEY = "arg.set-object-record-name.ctx"
        private const val ARG_SPACE_KEY = "arg.set-object-record-name.space"

        fun args(
            ctx: Id,
            space: Id
        ) = bundleOf(
            ARG_CTX_KEY to ctx,
            ARG_SPACE_KEY to space
        )
    }
}