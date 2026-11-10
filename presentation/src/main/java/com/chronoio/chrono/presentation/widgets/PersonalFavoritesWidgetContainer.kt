package com.chronoio.chrono.presentation.widgets

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.ui.objectIcon
import com.chronoio.chrono.domain.library.StoreSearchParams
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.getTypeOfObject
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.presentation.search.ObjectSearchConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * Container for the "My Favorites" section — an Unread-style compact list of
 * personal favorites for the current user in [space].
 *
 * Personal-favorite target IDs come from [ObservePersonalFavoriteTargets]; this
 * container only decides how to join them with full object details and emit a
 * [WidgetView.SetOfObjects] with `isCompact = true`. Block-tree observation
 * itself is shared with the object-menu options provider.
 */
class PersonalFavoritesWidgetContainer(
    private val space: SpaceId,
    private val widget: Widget.PersonalFavorites,
    private val observePersonalFavoriteTargets: ObservePersonalFavoriteTargets,
    private val storage: StorelessSubscriptionContainer,
    private val urlBuilder: UrlBuilder,
    private val fieldParser: FieldParser,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    isSessionActiveFlow: Flow<Boolean>
) : WidgetContainer {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val view: Flow<WidgetView> = isSessionActiveFlow
        .flatMapLatest { isActive ->
            if (!isActive) flowOf(emptyView())
            else observePersonalFavoriteTargets(space)
                .flatMapLatest { targets ->
                    if (targets.isEmpty()) flowOf(emptyView())
                    else joinWithObjects(targets)
                }
        }
        .catch { e ->
            Timber.e(e, "PersonalFavoritesWidgetContainer: view flow failed")
            emit(emptyView())
        }

    private fun joinWithObjects(targets: List<Id>): Flow<WidgetView> {
        val params = StoreSearchParams(
            space = space,
            subscription = "subscription.personal.favorites.${widget.id}",
            keys = ObjectSearchConstants.defaultKeys,
            filters = listOf(
                DVFilter(
                    relation = Relations.ID,
                    condition = DVFilterCondition.IN,
                    value = targets
                )
            ),
            limit = 0,
            source = emptyList(),
            collection = null,
            sorts = emptyList()
        )
        return storage.subscribe(params).map { objects ->
            val byId = objects.associateBy { it.id }
            val ordered = targets.mapNotNull { byId[it] }
            WidgetView.SetOfObjects(
                id = widget.id,
                source = widget.source,
                tabs = emptyList(),
                elements = ordered.map { obj ->
                    WidgetView.SetOfObjects.Element.Regular(
                        obj = obj,
                        objectIcon = obj.objectIcon(
                            builder = urlBuilder,
                            objType = storeOfObjectTypes.getTypeOfObject(obj)
                        ),
                        name = WidgetView.Name.Default(
                            prettyPrintName = fieldParser.getObjectNameOrPluralsForTypes(obj, false)
                        )
                    )
                },
                isExpanded = true,
                isCompact = true,
                name = WidgetView.Name.Empty,
                sectionType = widget.sectionType
            )
        }
    }

    private fun emptyView(): WidgetView = WidgetView.SetOfObjects(
        id = widget.id,
        source = widget.source,
        tabs = emptyList(),
        elements = emptyList(),
        isExpanded = true,
        isCompact = true,
        name = WidgetView.Name.Empty,
        sectionType = widget.sectionType
    )
}
