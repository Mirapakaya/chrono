package com.chronoio.chrono.presentation.sets.model

sealed class ViewerRelationListView {
    data class Relation(val view: SimpleRelationView) : ViewerRelationListView()
}
