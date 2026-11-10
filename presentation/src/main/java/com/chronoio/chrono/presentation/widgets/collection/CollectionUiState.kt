package com.chronoio.chrono.presentation.widgets.collection

import com.chronoio.chrono.domain.base.Resultat
import com.chronoio.chrono.presentation.objects.ObjectAction

class CollectionUiState(
    val views: Resultat<List<CollectionView>>,
    val showEditMode: Boolean,
    val showWidget: Boolean,
    val collectionName: String,
    val actionName: String,
    val objectActions: List<ObjectAction>,
    val inDragMode: Boolean,
    val displayType: Boolean,
    val operationInProgress: Boolean,
    val isActionButtonVisible: Boolean
)