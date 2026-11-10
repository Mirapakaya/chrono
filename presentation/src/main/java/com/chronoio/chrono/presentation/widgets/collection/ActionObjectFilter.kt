package com.chronoio.chrono.presentation.widgets.collection

import com.chronoio.chrono.presentation.objects.ObjectAction
import com.chronoio.chrono.presentation.objects.ObjectAction.ADD_TO_FAVOURITE
import com.chronoio.chrono.presentation.objects.ObjectAction.DELETE
import com.chronoio.chrono.presentation.objects.ObjectAction.DELETE_FILES
import com.chronoio.chrono.presentation.objects.ObjectAction.MOVE_TO_BIN
import com.chronoio.chrono.presentation.objects.ObjectAction.REMOVE_FROM_FAVOURITE
import com.chronoio.chrono.presentation.objects.ObjectAction.RESTORE
import javax.inject.Inject

class ActionObjectFilter @Inject constructor() {

    fun filter(subscription: Subscription, views: List<CollectionObjectView>): List<ObjectAction> {
        return when (subscription) {
            Subscription.Recent, Subscription.RecentLocal, Subscription.Sets, Subscription.Collections ->
                buildList {
                    add(MOVE_TO_BIN)
                }

            Subscription.Bin -> listOf(DELETE, RESTORE)
            Subscription.Favorites -> listOf(REMOVE_FROM_FAVOURITE, MOVE_TO_BIN)
            Subscription.None -> emptyList()
            Subscription.Files -> listOf(DELETE_FILES)
        }
    }

    private fun List<CollectionObjectView>.someFavorites() = any { it.obj.isFavorite }

    private fun List<CollectionObjectView>.someNonFavorites() = any { !it.obj.isFavorite }
}