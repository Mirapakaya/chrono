package com.chronoio.chrono.domain.search

import com.chronoio.chrono.core_models.Id

/**
 * Data view state for keeping track of results and its dependencies.
 */
sealed class DataViewState {
    /**
     * @property [objects] data for this subscription
     * @property [dependencies] its dependencies
     * @property [lastModified] monotonically increasing revision, bumped whenever the data —
     * including backing [com.chronoio.chrono.domain.objects.ObjectStore] content — changes.
     * Not a wall-clock timestamp: it exists so that states whose id lists are identical but
     * whose store content differs never compare equal (downstream relies on StateFlow equality
     * dedup to skip no-op re-renders).
     */
    data class Loaded(
        val objects: List<Id> = emptyList(),
        val dependencies: List<Id> = emptyList(),
        val lastModified: Long = 0L
    ) : DataViewState()

    data object Init : DataViewState()
}