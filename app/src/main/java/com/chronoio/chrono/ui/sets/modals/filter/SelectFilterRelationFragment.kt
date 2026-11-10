package com.chronoio.chrono.ui.sets.modals.filter

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_utils.ext.arg
import com.chronoio.chrono.core_utils.ext.withParent
import com.chronoio.chrono.di.common.componentManager
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.presentation.sets.SearchRelationViewModel
import com.chronoio.chrono.presentation.sets.SelectFilterRelationViewModel
import com.chronoio.chrono.presentation.sets.model.SimpleRelationView
import com.chronoio.chrono.ui.sets.modals.search.SearchRelationFragment
import javax.inject.Inject

class SelectFilterRelationFragment : SearchRelationFragment() {

    override val ctx: String get() = arg(CTX_KEY)
    val space: String get() = arg(SPACE_ID_KEY)
    override val viewer: String get() = arg(VIEWER_ID_KEY)

    @Inject
    lateinit var factory: SelectFilterRelationViewModel.Factory

    override val vm: SearchRelationViewModel by viewModels { factory }

    override fun onRelationClicked(ctx: Id, relation: SimpleRelationView) {
        withParent<CreateFilterFlow> { onRelationSelected(ctx, relation) }
    }

    override fun injectDependencies() {
        componentManager()
            .selectFilterRelationComponent
            .get(
                params = DefaultComponentParam(
                    ctx = ctx,
                    space = SpaceId(space)
                )
            )
            .inject(this)
    }

    override fun releaseDependencies() {
        componentManager().selectFilterRelationComponent.release()
    }

    companion object {
        fun new(ctx: Id, space: Id, viewerId: Id): SelectFilterRelationFragment = SelectFilterRelationFragment().apply {
            arguments = bundleOf(CTX_KEY to ctx, SPACE_ID_KEY to space, VIEWER_ID_KEY to viewerId)
        }

        const val CTX_KEY = "arg.select-filter-relation.ctx"
        const val SPACE_ID_KEY = "arg.select-filter-relation.space-id"
        const val VIEWER_ID_KEY = "arg.select-filter-relation.viewer"
    }
}