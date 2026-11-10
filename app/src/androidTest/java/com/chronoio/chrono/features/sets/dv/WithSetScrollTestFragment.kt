package com.chronoio.chrono.features.sets.dv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.feature_object_type.ui.*
import com.chronoio.chrono.presentation.sync.SyncStatusWidgetState
import com.chronoio.chrono.ui.primitives.WithSetScreen
import com.chronoio.chrono.ui.sets.ObjectSetFragment

/** Actual production type composition; only the embedded fragment's dependencies are replaced. */
class WithSetScrollTestFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
                if (className == ObjectSetFragment::class.java.name) TestObjectSetFragment()
                else super.instantiate(classLoader, className)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WithSetScreen(
                    uiSyncStatusBadgeState = UiSyncStatusBadgeState.Hidden,
                    uiSyncStatusState = SyncStatusWidgetState.Hidden,
                    uiIconState = UiIconState.EMPTY,
                    uiTitleState = UiTitleState("Runtime Leads", "Runtime Leads", false),
                    uiDescriptionState = UiDescriptionState("Type description for native scroll validation", true, true),
                    uiHorizontalButtonsState = UiHorizontalButtonsState(
                        UiPropertiesButtonState.Visible(3), UiLayoutButtonState.Visible(ObjectType.Layout.BASIC),
                        UiTemplatesButtonState.Visible(2), true),
                    uiLayoutTypeState = UiLayoutTypeState.Hidden,
                    uiTemplatesModalListState = UiTemplatesModalListState.Hidden.EMPTY,
                    uiDeleteAlertState = UiDeleteAlertState.Hidden,
                    uiDeleteTypeAlertState = UiDeleteTypeAlertState.Hidden,
                    onTypeEvent = {},
                    objectId = requireArguments().getString(ObjectSetFragment.CONTEXT_ID_KEY)!!,
                    space = requireArguments().getString(ObjectSetFragment.SPACE_ID_KEY)!!
                )
            }
        }

    fun dataview(): ObjectSetFragment = childFragmentManager.fragments.filterIsInstance<ObjectSetFragment>().single()
}
