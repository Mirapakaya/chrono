package com.chronoio.chrono.ui.publishtoweb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_utils.ext.safeNavigate
import com.chronoio.chrono.core_utils.ext.toast
import com.chronoio.chrono.core_utils.intents.ActivityCustomTabsHelper
import com.chronoio.chrono.core_utils.ui.BaseBottomSheetComposeFragment
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.presentation.publishtoweb.MySitesViewModel
import com.chronoio.chrono.ui.chats.ChatFragment
import com.chronoio.chrono.ui.editor.EditorFragment
import com.chronoio.chrono.ui.home.WidgetsScreenFragment
import com.chronoio.chrono.ui.settings.typography
import timber.log.Timber
import javax.inject.Inject

class MySitesFragment : BaseBottomSheetComposeFragment() {

    @Inject
    lateinit var factory: MySitesViewModel.Factory

    private val vm by viewModels<MySitesViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Capture Fragment-scope references outside the @Composable lambda — inside
        // `content { ... }` the implicit receiver is Composable, not Fragment, so
        // `findNavController()`, `requireContext()`, `requireActivity()` don't resolve.
        val fragment = this
        val ctx = requireContext()
        val activity = requireActivity()
        return content {
            MaterialTheme(typography = typography) {
                val clipboardManager = LocalClipboardManager.current

                MySitesScreen(
                    viewState = vm.viewState.collectAsStateWithLifecycle().value,
                    onViewObjectClicked = vm::onOpenObject,
                    onOpenInBrowserClicked = vm::onOpenInBrowser,
                    onCopyWebLinkClicked = vm::onCopyWebLink,
                    onUnpublishClicked = vm::onUnpublishClicked,
                    onBackClicked = {
                        fragment.findNavController().popBackStack()
                    }
                )
                LaunchedEffect(Unit) {
                    vm.commands.collect { command ->
                        Timber.d("MySites New command: $command")
                        when (command) {
                            is MySitesViewModel.Command.ShowToast -> {
                                ctx.toast(command.message)
                            }
                            is MySitesViewModel.Command.CopyToClipboard -> {
                                clipboardManager.setText(AnnotatedString(command.text))
                            }
                            is MySitesViewModel.Command.Browse -> {
                                ActivityCustomTabsHelper.openUrl(
                                    activity = activity,
                                    url = command.url
                                )
                            }
                            is MySitesViewModel.Command.OpenObject -> {
                                fragment.findNavController().safeNavigateOrLog(
                                    id = R.id.objectNavigation,
                                    args = EditorFragment.args(
                                        ctx = command.objectId,
                                        space = command.spaceId.id
                                    ),
                                    errorTag = "object from my-sites",
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skipCollapsed()
        expand()
    }

    private fun navigateToHomeOrChatThen(
        nav: NavController,
        chat: Id?,
        space: Id,
        then: () -> Unit
    ) {
        if (chat.isNullOrBlank()) {
            nav.safeNavigateOrLog(
                id = R.id.homeScreen,
                args = WidgetsScreenFragment.args(deeplink = null, space = space)
            )
        } else {
            nav.safeNavigateOrLog(
                id = R.id.chatScreen,
                args = ChatFragment.args(space = space, ctx = chat)
            )
        }
        then()
    }

    private fun NavController.safeNavigateOrLog(
        @IdRes id: Int,
        args: Bundle? = null,
        errorTag: String? = null
    ) {
        val currentId = currentDestination?.id ?: return
        safeNavigate(currentId, id, args, errorTag)
    }

    override fun injectDependencies() {
        componentManager()
            .mySitesComponent
            .get(params = MySitesViewModel.VmParams)
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().mySitesComponent.release()
    }
}